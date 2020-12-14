package com.test.service.impl;

import com.test.dto.UserDTO;
import com.test.model.User;
import com.test.repository.UserRepository;
import com.test.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
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
        String sql ="SELECT userName from user u LEFT JOIN staff s ON u.staff_id = s.id where s.name = :staffName";
        return userRepository.nativeSQLQuery(sql,queryParam, UserDTO.class);
    }


    @Override
    public Page<User> page(Map<String, Object> queryParam, Pageable pageable) {
        String sql ="SELECT * from user u LEFT JOIN staff s ON u.staff_id = s.id ";
        String countSql ="SELECT count(*) from user u LEFT JOIN staff s ON u.staff_id = s.id ";
        return userRepository.nativeSQLQuery(sql,countSql,queryParam,pageable);
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
