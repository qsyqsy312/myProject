package com.test.service.base;

import com.test.dto.base.BaseDTO;
import com.test.model.base.BaseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IShardingService<T extends BaseModel, ID extends Serializable> {
    void createTable(String tableName);
}
