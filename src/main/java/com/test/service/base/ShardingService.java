package com.test.service.base;

import com.test.dto.base.BaseDTO;
import com.test.model.base.BaseModel;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.core.rule.TableRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * TODO: 水平分表
 * @param <T>
 * @param <ID>
 */
public abstract class ShardingService<T extends BaseModel, ID extends Serializable> extends BaseService<T,ID> implements IShardingService<T,ID>{


    protected PreciseShardingAlgorithm preciseShardingAlgorithm;
    protected RangeShardingAlgorithm rangeShardingAlgorithm;
    protected TableRule tableRule;



    @Autowired
    protected ApplicationContext applicationContext;

    @PostConstruct
    public abstract void init();

    @Override
    public BaseDTO save(BaseDTO dto) throws Exception {
        T t = toEntity(dto,baseDao.getDomainClazz().newInstance());
        fillSaveValue(t);
        customIDGenerator(t);
        createTable(t);
        return toDTO(baseDao.customSave(t));
    }

    @Override
    public BaseDTO update(BaseDTO dto) {
        T one = baseDao.findOneById((ID) dto.getId());
        T t = toEntity(dto,one);
        fillSaveValue(t);
        createTable(t);
        return toDTO(baseDao.customUpdate(t));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public T findOneById(ID id) {
        return super.findOneById(id);
    }

    @Override
    public void deleteByIds(Iterable<ID> ids) {
        super.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<BaseDTO> list(Map<String, Object> queryParam, Sort sort) {
        createTable(queryParam);
        return super.list(queryParam, sort);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Page<BaseDTO> page(Map<String, Object> queryParam, Pageable pageable) {
        createTable(queryParam);
        return super.page(queryParam, pageable);
    }


}
