package com.test.repository;

import com.test.model.User;
import com.test.repository.base.BaseDao;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseDao<User,String> {
}
