package com.test.service.impl;

import com.test.model.User;
import com.test.repository.UserRepository;
import com.test.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Map;

@Service
public class UserService implements IUserService {


    @Autowired
    private UserRepository userRepository;


    @Override
    public List<User> list(Map<String, Object> queryParam) {
        return userRepository.findAll(getSpecification(queryParam));
    }

    @Override
    public Specification<User> getSpecification(Map<String, Object> queryParam) {
        Specification<User> specification = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if(queryParam.get("staffName")!=null){
                    Join<Object, Object> staff = root.join("staff");
                    predicate.getExpressions().add(criteriaBuilder.equal(staff.get("name"),queryParam.get("staffName")));
                }
                return predicate;
            }
        };
        return specification;
    }
}
