package com.test.service.impl;

import com.google.common.collect.BoundType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.test.dto.UserDTO;
import com.test.dto.base.BaseDTO;
import com.test.model.User;
import com.test.service.IUserService;
import com.test.service.base.StandardShardingService;
import org.apache.shardingsphere.core.strategy.route.value.ListRouteValue;
import org.apache.shardingsphere.core.strategy.route.value.RangeRouteValue;
import org.apache.shardingsphere.core.strategy.route.value.RouteValue;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.*;

@Service
public class UserService extends StandardShardingService<User, String> implements IUserService {


    @Override
    public User toEntity(BaseDTO dto, User entity) {
        UserDTO userDTO = (UserDTO) dto;
        entity.setUserName(userDTO.getUserName());
        entity.setRegisterTime(userDTO.getRegisterTime());
//        Staff staff = new Staff();
//        staff.setId("1");
//        entity.setStaff(staff);
        return entity;
    }

    @Override
    public Object toDTO(User entity,Object ... data) {
        UserDTO dto = new UserDTO();
        dto.setUserName(entity.getUserName());
        dto.setId(entity.getId());
        return dto;
    }

    @Override
    public void customIDGenerator(User user) {
        user.setId(UUID.randomUUID().toString().replaceAll("-", "")+"-"+new DateTime(user.getRegisterTime()).toString("yyyyMMddHHmmss"));
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
                    predicate.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.get("registerTime").as(Date.class), startTime.toDate()));
                    predicate.getExpressions().add(criteriaBuilder.lessThanOrEqualTo(root.get("registerTime").as(Date.class), endTime.toDate()));
                }
                return predicate;
            }
        };
        return baseDao.getBaseSpecification(queryParam).and(querySpec);
    }

    @Override
    public void createTable(User user) {
        Collection<RouteValue> shardingValue = Lists.newArrayList();
        shardingValue.add(new ListRouteValue<>("id","tableRule",Arrays.asList(user.getId())));
        shardingValue.add(new ListRouteValue<>("registerTime",tableRule.getLogicTable(),Arrays.asList(user.getCreateTime())));
        Collection<String> tableNames = shardingStrategy.doSharding(this.tableRule.getActualTableNames("db0"), shardingValue,null);
        EntityManager entityManager = baseDao.getEntityManager();
        for(String tableName:tableNames){
            entityManager.createNativeQuery("CREATE TABLE IF NOT EXISTS " + tableName + " LIKE " + baseDao.getTableName()).executeUpdate();
        }
    }

    @Override
    public void createTable(Map<String, Object> queryParam) {
        EntityManager entityManager = baseDao.getEntityManager();
        LocalDate startTime = LocalDate.parse((String) queryParam.get("startTime"));
        LocalDate endTime = LocalDate.parse((String) queryParam.get("endTime"));
        Collection<RouteValue> shardingValue =  Lists.newArrayList();
        shardingValue.add(new RangeRouteValue<>("registerTime",tableRule.getLogicTable(),Range.range(startTime.toDate(), BoundType.CLOSED, endTime.toDate(), BoundType.CLOSED)));
        Collection<String> tableNames = shardingStrategy.doSharding(this.tableRule.getActualTableNames("db0"), shardingValue,null);
        for (String tableName : tableNames) {
            entityManager.createNativeQuery("CREATE TABLE IF NOT EXISTS " + tableName + " LIKE " + baseDao.getTableName()).executeUpdate();
        }
    }


}
