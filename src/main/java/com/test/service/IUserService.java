package com.test.service;

import com.test.model.User;
import com.test.service.base.IShardingService;

public interface IUserService extends IShardingService<User,String> {

}
