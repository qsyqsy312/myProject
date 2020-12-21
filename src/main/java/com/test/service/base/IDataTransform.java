package com.test.service.base;

import com.test.dto.base.BaseDTO;
import com.test.model.base.BaseModel;

import java.io.Serializable;


/**
 * 持久化之前以及之后的数据处理接口
 * @param <T>
 * @param <ID>
 */
public interface IDataTransform<T extends BaseModel, ID extends Serializable> {

    T toEntity(BaseDTO dto,T entity);

    /**
     * 返回类型不限制，可加入别的任意DTO组装
     * @param entity
     * @param params
     * @return
     */
    Object toDTO(T entity);

    void customIDGenerator(T t);
}
