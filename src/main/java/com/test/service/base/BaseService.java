package com.test.service.base;

import com.test.dto.base.BaseDTO;
import com.test.model.base.BaseModel;
import com.test.repository.base.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * 普通CRUD实现类
 * @param <T>
 * @param <ID>
 */
public abstract class BaseService<T extends BaseModel, ID extends Serializable> implements IBaseService<T, ID>,IDataTransform<T,ID>{


    protected BaseDao<T, ID> baseDao;


    @Autowired
    public void setBaseDao(BaseDao<T, ID> baseDao) {
        this.baseDao = baseDao;
    }



    @Override
    public Specification<T> getSpecification(Map<String, Object> queryParam) {
        return baseDao.getBaseSpecification(queryParam);
    }


    //默认实现，UUID
    @Override
    public void customIDGenerator(T t){
        if (t.getId() == null) {
            t.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        }
    }



    protected void fillSaveValue(T t) {
        if (t instanceof BaseModel) {
            ((BaseModel) t).setLastModifyTime(new Date());
            if (StringUtils.isEmpty(((BaseModel) t).getId())) {
                ((BaseModel) t).setCreateTime(new Date());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public BaseDTO save(BaseDTO dto) {
        T t = toEntity(dto,null);
        fillSaveValue(t);
        customIDGenerator(t);
        return toDTO(baseDao.customSave(t));
    }


    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public BaseDTO update(BaseDTO dto) {
        T one = baseDao.findOneById((ID) dto.getId());
        T t = toEntity(dto,one);
        fillSaveValue(t);
        return toDTO(baseDao.customUpdate(t));
    }

    @Override
    @Transactional(readOnly = true)
    public T findOneById(ID id) {
        return baseDao.findById(id).orElseThrow(()->new RuntimeException("查询数据不存在！"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Iterable<ID> ids) {
        if (ids != null) {
            while (ids.iterator().hasNext()) {
                baseDao.customDeleteById(ids.iterator().next());
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BaseDTO> list(Map<String, Object> queryParam, Sort sort) {
        return baseDao.findAll(getSpecification(queryParam), sort).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BaseDTO> page(Map<String, Object> queryParam, Pageable pageable) {
        Page<T> page = baseDao.findAll(getSpecification(queryParam), pageable);
        return page.map(this::toDTO);
    }
}
