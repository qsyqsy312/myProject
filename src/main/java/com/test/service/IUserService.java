package com.test.service;

import com.test.model.Staff;
import com.test.model.User;
import com.test.service.base.IBaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IUserService extends IBaseService<User,String> {

    List<User> list(Map<String,Object> queryParam);

    Page<User> page(Map<String,Object> queryParam, Pageable pageable);
}
