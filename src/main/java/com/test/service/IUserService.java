package com.test.service;

import com.test.model.Staff;
import com.test.model.User;
import com.test.service.base.IBaseService;

import java.util.List;
import java.util.Map;

public interface IUserService extends IBaseService<User,String> {

    List<User> list(Map<String,Object> queryParam);
}
