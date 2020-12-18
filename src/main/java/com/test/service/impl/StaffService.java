package com.test.service.impl;

import com.test.dto.StaffDTO;
import com.test.dto.base.BaseDTO;
import com.test.model.Staff;
import com.test.repository.StaffRepository;
import com.test.service.IStaffService;
import com.test.service.base.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class StaffService extends BaseService<Staff,String> implements IStaffService {



    @Override
    public Specification<Staff> getSpecification(Map<String,Object> queryParam) {
        Specification<Staff> specification = new Specification<Staff>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder)  {
                Predicate predicate = criteriaBuilder.conjunction();
                if(queryParam.get("name")!=null){
                    String name = (String) queryParam.get("name");
                    predicate.getExpressions().add(criteriaBuilder.like(root.get("name"),"%"+name+"%"));
                }
                if(queryParam.get("phone")!=null){
                    predicate.getExpressions().add(criteriaBuilder.equal(root.get("name"),queryParam.get("phone")));
                }
                if(queryParam.get("age")!=null){
                    predicate.getExpressions().add(criteriaBuilder.equal(root.get("age").as(Integer.class),queryParam.get("age")));
                }
                return predicate;
            }
        };
        return specification;
    }

    @Override
    public Staff toEntity(BaseDTO dto, Staff entity) {
         BeanUtils.copyProperties(dto,entity);
         return entity;
    }

    @Override
    public BaseDTO toDTO(Staff entity) {
        StaffDTO staffDTO =new StaffDTO();
        BeanUtils.copyProperties(entity,staffDTO);
        return staffDTO;
    }
}
