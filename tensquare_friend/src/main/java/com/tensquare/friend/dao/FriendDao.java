package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 交友微服务 持久层
 */
public interface FriendDao extends JpaRepository<Friend,String>{
    /**
     * 根据好友id和用户id查询当前用户是否已经关注此好友
     * @param friendid
     * @param userid
     * @return
     */
    @Query("select count(*) from Friend f where f.friendid = ?1 and f.userid =?2 ")
    int selectCount(String friendid, String userid);

    /**
     * 根据用户id和好友id更新islike状态
     * @param userid
     * @param friendid
     */
    /*@Query("update Friend f set f.islike = 1 where f.userid = ?1 and f.friendid = ?2 ")
    @Modifying
    void updateLike(String userid, String friendid);*/

    @Query("update Friend f set f.islike = ?3 where f.userid = ?1 and f.friendid = ?2 ")
    @Modifying
    void updateLike(String userid, String friendid,String islike);

    /**
     * 删除当前用户关注的好友记录
     * @param friendid
     * @param userid
     */
    @Query("delete from Friend f where f.friendid = ?1 and f.userid = ?2")
    @Modifying
    void deleteFriend(String friendid, String userid);
}
