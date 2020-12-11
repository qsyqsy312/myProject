package com.test.service.impl;

import com.test.model.User;
import com.test.service.IUserService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService implements IUserService {


    @Override
    public Specification<User> getSpecification(Map<String, Object> queryParam) {
        return null;
    }
}
