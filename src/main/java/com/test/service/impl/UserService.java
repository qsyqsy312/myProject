package com.test.service.impl;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import com.test.dto.UserDTO;
import com.test.dto.base.BaseDTO;
import com.test.model.Staff;
import com.test.model.User;
import com.test.service.IUserService;
import com.test.service.base.ShardingService;
import com.test.util.UserShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.core.rule.TableRule;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.joda.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

@Service
public class UserService extends ShardingService<User, String> implements IUserService {

    @Override
    public void init() {
        ShardingDataSource bean = applicationContext.getBean(ShardingDataSource.class);
        Collection<TableRule> tableRules = bean.getRuntimeContext().getRule().getTableRules();
        for (TableRule tableRule : tableRules) {
            if (User.TABLE_NAME.equals(tableRule.getLogicTable())) {
                this.tableRule = tableRule;
                break;
            }
        }
        this.preciseShardingAlgorithm = applicationContext.getBean(UserShardingAlgorithm.class);
        this.rangeShardingAlgorithm = applicationContext.getBean(UserShardingAlgorithm.class);
        Assert.notNull(tableRule,"分表规则未配置！");
    }

    @Override
    public User toEntity(BaseDTO dto, User entity) {
        UserDTO userDTO = (UserDTO) dto;
        entity.setUserName(userDTO.getUserName());
        Staff staff = new Staff();
        staff.setId("1");
        entity.setStaff(staff);
        return entity;
    }

    @Override
    public BaseDTO toDTO(User entity) {
        UserDTO dto = new UserDTO();
        dto.setUserName(entity.getUserName());
        dto.setId(entity.getId());
        return dto;
    }

    @Override
    public Specification<User> getSpecification(Map<String, Object> queryParam) {
        Specification<User> querySpec = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if (queryParam.get("staffName") != null) {
                    Join<Object, Object> staff = root.join("staff");
                    predicate.getExpressions().add(criteriaBuilder.equal(staff.get("name"), queryParam.get("staffName")));
                }
                if (queryParam.get("startTime") != null && queryParam.get("endTime") != null) {
                    LocalDate startTime = LocalDate.parse((String) queryParam.get("startTime"));
                    LocalDate endTime = LocalDate.parse((String) queryParam.get("endTime"));
                    predicate.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.get("createTime").as(Date.class), startTime.toDate()));
                    predicate.getExpressions().add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime").as(Date.class), endTime.toDate()));
                }
                return predicate;
            }
        };
        return baseDao.getBaseSpecification(queryParam).and(querySpec);
    }


    @Override
    public void createTable(String id) {

    }

    @Override
    public void createTable(User user) {
        String tableName = preciseShardingAlgorithm.doSharding(this.tableRule.getActualTableNames("db0"), new PreciseShardingValue("", "", user.getCreateTime()));
        EntityManager entityManager = baseDao.getEntityManager();
        entityManager.createNativeQuery("CREATE TABLE IF NOT EXISTS " + tableName + " LIKE user ").executeUpdate();
    }

    @Override
    public void createTable(Map<String, Object> queryParam) {
        EntityManager entityManager = baseDao.getEntityManager();
        LocalDate startTime = LocalDate.parse((String) queryParam.get("startTime"));
        LocalDate endTime = LocalDate.parse((String) queryParam.get("endTime"));
        RangeShardingValue rangeShardingValue = new RangeShardingValue("", "", Range.range(startTime.toDate(), BoundType.CLOSED, endTime.toDate(), BoundType.CLOSED));
        Collection<String> tableNames = rangeShardingAlgorithm.doSharding(this.tableRule.getActualTableNames("db0"), rangeShardingValue);
        for (String tableName : tableNames) {
            entityManager.createNativeQuery("CREATE TABLE IF NOT EXISTS " + tableName + " LIKE user ").executeUpdate();
        }
    }



}
