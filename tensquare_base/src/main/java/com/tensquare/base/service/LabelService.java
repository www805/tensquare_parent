package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 标签业务逻辑处理层
 */
@Service
public class LabelService {

    @Autowired
    private LabelDao labelDao;

    @Autowired
    private IdWorker idWorker;

    public void save(Label label) {
        //id 分布式id
        label.setId(idWorker.nextId() + "");
        labelDao.save(label);
    }

    public void deleteById(String labelId) {
        labelDao.deleteById(labelId);
    }

    public void updateById(Label label, String labelId) {
        label.setId(labelId);
        labelDao.save(label);
    }

    public Label findById(String labelId) {

        return labelDao.findById(labelId).get();
    }

    public List<Label> findAll() {
        return labelDao.findAll();
    }

    /**
     * 带条件的查询
     *
     * @param map
     * @return
     */
    public List<Label> findAll(Map<String, String> map) {
        //拼接查询条件
        /*Specification<Label> specification = new Specification<Label>() {
            //root:需要操作的实体对象  操作对象来操作数据库 通过root实体对象获取属性拼接条件
            //criteriaQuery:顶级查询条件
            //criteriaBuilder:负责拼接条件的
            @Nullable
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                // "labelname": "string",
                //"state": "string",
                String labelname = map.get("labelname");
                String state = map.get("state");
                //定一个list集合来存放 条件
                List<Predicate> predicateList = new ArrayList<>();
                //将list转数组
                if (!StringUtils.isEmpty(labelname)) {
                    //第一个条件
                    Predicate p1 = criteriaBuilder.like(root.get("labelname").as(String.class), "%" + labelname + "%");// lablename like  ?
                    predicateList.add(p1);
                }
                if (!StringUtils.isEmpty(state)) {
                    //第二个条件
                    Predicate p2 = criteriaBuilder.equal(root.get("state").as(String.class), state);// state =  ?
                    predicateList.add(p2);
                }
                if (predicateList == null || predicateList.size() == 0) {
                    return null;
                }
                //定一个数组对象来存放条件Predicate
                *//*Predicate[] p = new Predicate[predicateList.size()];
                Predicate[] predicates = predicateList.toArray(p);
                //criteriaBuilder 拼接条件 and
                return criteriaBuilder.and(predicates);*//*
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };*/
        return labelDao.findAll(getSpecification(map));
    }


    /**
     * 抽取条件查询方法
     *
     */
    public Specification<Label> getSpecification(Map<String, String> map){
        return new Specification<Label>() {
            //root:需要操作的实体对象  操作对象来操作数据库 通过root实体对象获取属性拼接条件
            //criteriaQuery:顶级查询条件
            //criteriaBuilder:负责拼接条件的
            @Nullable
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                // "labelname": "string",
                //"state": "string",
                String labelname = map.get("labelname");
                String state = map.get("state");
                //定一个list集合来存放 条件
                List<Predicate> predicateList = new ArrayList<>();
                //将list转数组
                if (!StringUtils.isEmpty(labelname)) {
                    //第一个条件
                    Predicate p1 = criteriaBuilder.like(root.get("labelname").as(String.class), "%" + labelname + "%");// lablename like  ?
                    predicateList.add(p1);
                }
                if (!StringUtils.isEmpty(state)) {
                    //第二个条件
                    Predicate p2 = criteriaBuilder.equal(root.get("state").as(String.class), state);// state =  ?
                    predicateList.add(p2);
                }
                if (predicateList == null || predicateList.size() == 0) {
                    return null;
                }
                //定一个数组对象来存放条件Predicate
                /*Predicate[] p = new Predicate[predicateList.size()];
                Predicate[] predicates = predicateList.toArray(p);
                //criteriaBuilder 拼接条件 and
                return criteriaBuilder.and(predicates);*/
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }

    /**
     * 带分页的条件查询
     */
    public Page<Label> findAll(int page, int size, Map<String, String> map) {
        Pageable pageable = PageRequest.of(page-1,size);
        return labelDao.findAll(getSpecification(map),pageable);
    }
}
