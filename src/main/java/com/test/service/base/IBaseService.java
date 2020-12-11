package com.test.service.base;

import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.Map;

public interface IBaseService<T,ID extends Serializable> {

    Specification<T> getSpecification(Map<String,Object> queryParam);
}
