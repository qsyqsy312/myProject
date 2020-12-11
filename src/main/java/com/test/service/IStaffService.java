package com.test.service;

import com.test.model.Staff;
import com.test.service.base.IBaseService;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IStaffService extends IBaseService<Staff,String> {

    List<Staff> list(Map<String,Object> queryParam);
}
