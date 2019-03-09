package com.tensquare.qa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.qa.pojo.Problem;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{
    /**
     * select * from tb_problem p where p.id in(select  problemid  from tb_pl   where  labelid=1  )
     *
     * Pageable pageable 内部对象处理
     * 最新问答列表
     * @param labelid 页面标签id
     */
    @Query(value = "select p from Problem p where p.id in (select problemid from Pl where labelid = ?1) order by replytime desc")
    //@Query("select * from tb_problem p where p.id in(select  problemid  from tb_pl   where  labelid=1  ) and order by replytime desc",nativeQuery = true)
    Page<Problem> newlist(Pageable pageable, String labelid);
    /**
     * 热门问答列表
     *
     * @param labelid 页面标签id
     */
    @Query("select p from Problem p where p.id in (select problemid from Pl where labelid = ?1) order by reply desc")
    Page<Problem> hotlist(Pageable pageable, String labelid);
    /**
     * 等待回答列表
     *
     * @param labelid 页面标签id
     */
    @Query("select p from Problem p where p.id in (select problemid from Pl where labelid = ?1) and reply = 0 order by replytime desc")
    Page<Problem> waitlist(Pageable pageable, String labelid);
}
