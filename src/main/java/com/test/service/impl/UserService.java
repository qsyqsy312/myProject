package com.test.service.impl;

import com.test.dto.UserDTO;
import com.test.dto.base.BaseDTO;
import com.test.model.Staff;
import com.test.model.User;
import com.test.service.IUserService;
import com.test.service.base.BaseService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.Map;

@Service
public class UserService extends BaseService<User, String> implements IUserService {




    @Override
    public User toEntity(BaseDTO dto, User entity) {
        UserDTO userDTO = (UserDTO) dto;
        if (entity == null) {
            entity = new User();
        }
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
                return predicate;
            }
        };
        return baseDao.getBaseSpecification(queryParam).and(querySpec);
    }
}
