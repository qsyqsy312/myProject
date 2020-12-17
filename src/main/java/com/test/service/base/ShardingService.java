package com.test.service.base;

import com.test.dto.base.BaseDTO;
import com.test.model.base.BaseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * TODO: 水平分表
 * @param <T>
 * @param <ID>
 */
public abstract class ShardingService<T extends BaseModel, ID extends Serializable> implements IBaseService<T,ID>,IShardingService<T,ID>{

    @Override
    public Specification<T> getSpecification(Map<String, Object> queryParam) {
        return null;
    }

    @Override
    public BaseDTO save(BaseDTO dto) {
        return null;
    }

    @Override
    public BaseDTO update(BaseDTO dto) {
        return null;
    }

    @Override
    public void deleteByIds(Iterable<ID> ids) {

    }

    @Override
    public T findOneById(ID id) {
        return null;
    }

    @Override
    public List<BaseDTO> list(Map<String, Object> queryParam, Sort sort) {
        return null;
    }

    @Override
    public Page<BaseDTO> page(Map<String, Object> queryParam, Pageable pageable) {
        return null;
    }

    @Override
    public void createTable(String tableName) {

    }
}
