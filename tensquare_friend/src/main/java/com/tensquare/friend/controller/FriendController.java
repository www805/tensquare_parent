package com.tensquare.friend.controller;

import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 交友微服务-控制层
 */
@RestController
@RequestMapping("/friend")
@CrossOrigin
public class FriendController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private FriendService friendService;

    /**
     * 添加好友或非好友
     * @param friendid 添加或删除好友的id
     * @param type 类型 1:喜欢 2：不喜欢
     * @return
     */
    @RequestMapping(value = "/like/{friendid}/{type}",method = RequestMethod.PUT)
    public Result isLike(@PathVariable String friendid,@PathVariable String type){
        //鉴权 确认当前是否有权限操作功能
        Claims claims = (Claims)request.getAttribute("user_claims");
        if(claims == null || !claims.get("roles").equals("user")){
            return new Result(false, StatusCode.ACCESSERROR,"权限不足");
        }

        if(type.equals("1")){
            //喜欢 关注好友表 往tb_friend表插入数据
            //friendid:好友id  claims.getId :登录用户id
            int rs = friendService.like(friendid,claims.getId());//rs:表示添加（关注）好友是否成功 rs 1:已经关注此好友 0：未关注 关注成功
            if(rs == 1){
                return new Result(false, StatusCode.REPERROR,"已经关注次好友，无需重复关注");
            }
        }
        else
        {
            //不喜欢 插入到非好友表 往tb_nofriend表插入数据
            friendService.noLike(friendid,claims.getId());
        }
        return new Result(true, StatusCode.OK,"操作成功");
    }

    /**
     * /friend 删除好友
     */
    @RequestMapping(value = "/{friendid}",method = RequestMethod.DELETE)
    public Result deleteFriend(@PathVariable String friendid){
        //鉴权 确认当前是否有权限操作功能
        Claims claims = (Claims)request.getAttribute("user_claims");
        if(claims == null || !claims.get("roles").equals("user")){
            return new Result(false, StatusCode.ACCESSERROR,"权限不足");
        }
        friendService.deleteFriend(friendid,claims.getId());
        return new Result(true, StatusCode.OK,"删除好友成功");
    }
}
