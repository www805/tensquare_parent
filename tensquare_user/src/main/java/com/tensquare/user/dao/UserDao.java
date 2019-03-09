package com.tensquare.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.user.pojo.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface UserDao extends JpaRepository<User,String>,JpaSpecificationExecutor<User>{

    /**
     * 根据手机查询用户对象
     * @param mobile
     * @return
     */
    User findByMobile(String mobile);

    /**
     * 变更粉丝数
     * @param userid
     * @param x
     */
    @Query("update User u set u.fanscount = u.fanscount + ?2 where u.id = ?1")
    @Modifying
    void incFanscount(String userid, int x);

    /**
     * 变更关注数
     * @param userid
     * @param x
     */
    @Query("update User u set u.followcount = u.followcount + ?2 where u.id = ?1")
    @Modifying
    void incFollowcount(String userid, int x);
}
