package com.test.service.base;

import com.test.model.base.BaseModel;

import java.io.Serializable;
import java.util.Map;

public interface IShardingService<T extends BaseModel, ID extends Serializable> extends IBaseService<T,ID> {
    void createTable(T t);
    void createTable(Map<String,Object> queryParam);
}
