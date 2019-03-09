package com.tensquare.friend.service;

import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/***
 * 交友微服务-业务处理层
 */
@Service
public class FriendService {
    @Autowired
    private FriendDao friendDao;


    @Autowired
    private NoFriendDao noFriendDao;


    @Autowired
    private UserClient userClient;

    /**
     * 添加（关注）好友
     * @param friendid  好友id
     * @param userid 用户id
     * @return
     */
    @Transactional
    public int like(String friendid, String userid) {
        //1.判断当前登录用户是否已经关注此好友
        //通过自定义语句查询 是否已经关注好友 select count(*) from tb_friend where friendid = xx and userid =yyy
        int count = friendDao.selectCount(friendid,userid);
        //2.如果已经关注 则返回1
        if(count > 0 ){
            return 1;
        }
        //3.如果未关注 则添加好友往tb_friend表插入数据
        Friend friend = new Friend();
        friend.setFriendid(friendid);
        friend.setUserid(userid);
        friend.setIslike("0");
        friendDao.save(friend);

        //4.如果对方已经关注我，更新当前记录和好友记录中islike为1
        if(friendDao.selectCount(userid,friendid) > 0){
            friendDao.updateLike(userid,friendid,"1");
            friendDao.updateLike(friendid,userid,"1");
        }
        //5.变更关注 （当前登录的用户）
        userClient.incFollowcount(userid,1);
        //6.变更粉丝数（被关注的好友）
        userClient.incFanscount(friendid,1);
        return 0; //添加（关注）好友成功
    }

    /**
     * 添加非好友
     * @param friendid
     * @param userid
     * @return
     */
    public void noLike(String friendid, String userid){
        NoFriend noFriend = new NoFriend();
        noFriend.setFriendid(friendid);
        noFriend.setUserid(userid);
        noFriendDao.save(noFriend);
    }

    /**
     * 删除好友
     * @param friendid
     * @param userid
     */
    @Transactional
    public void deleteFriend(String friendid, String userid) {
        //1将当前登录用户关注的好友记录删除
        friendDao.deleteFriend(friendid,userid);
        //2将好友关注当前登录用户记录islike修改0  抽取代码
        friendDao.updateLike(friendid,userid,"0");

        //3.变更关注 （当前登录的用户）
        userClient.incFollowcount(userid,-1);
        //4.变更粉丝数（被关注的好友）
        userClient.incFanscount(friendid,-1);
    }
}
