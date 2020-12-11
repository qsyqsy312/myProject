package com.test.repository.base;

import com.test.model.base.BakDeleteModel;
import com.test.model.base.BaseModel;
import org.apache.tomcat.jni.User;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BaseDaoImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseDao<T, ID> {

    private static final int BATCH_SIZE = 500;

    private final EntityManager em;

    public BaseDaoImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager em) {
        super(entityInformation, em);
        this.em = em;

    }

    public BaseDaoImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.em = em;
    }



    private void fillSaveValue(T t) {
        if (t instanceof BaseModel) {
            ((BaseModel) t).setLastModifyTime(new Date());
            if (StringUtils.isEmpty(((BaseModel) t).getId())) {
                ((BaseModel) t).setCreateTime(new Date());
            }
        }
    }


    @Override
    public T customSave(T t) {
        fillSaveValue(t);
        return save(t);
    }

    @Override
    public <S extends T> Iterable<S> batchCustomSave(Iterable<S> var1) {
        List<S> list = new ArrayList<>(BATCH_SIZE);
        int num = 0;
        while (var1.iterator().hasNext()) {
            S s = var1.iterator().next();
            fillSaveValue(s);
            list.add(s);
            num++;
            em.persist(s);
            if (num % BATCH_SIZE == 0) {
                em.flush();
            }
        }
        return list;
    }

    @Override
    public void customDeleteById(ID id) {
        T t = findById(id).orElseThrow(() -> new RuntimeException("删除的数据不存在！"));
        if (t instanceof BakDeleteModel) {
            ((BakDeleteModel) t).setDeleteStatus(true);
            ((BakDeleteModel) t).setDeleteTime(new Date());
            em.merge(t);
        }else {
            delete(t);
        }
    }
}
