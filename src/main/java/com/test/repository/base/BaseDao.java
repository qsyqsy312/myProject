package com.test.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseDao<T,ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    T customSave(T t);

    <S extends T> Iterable<S> batchCustomSave(Iterable<S> var1);

    void customDeleteById(ID id);
}
