package com.tensquare.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器层
 *
 * @author Administrator
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", userService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", userService.findById(id));
    }


    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<User> pageList = userService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<User>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", userService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param user
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody User user) {
        userService.add(user);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param user
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody User user, @PathVariable String id) {
        user.setId(id);
        userService.update(user);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private HttpServletRequest request;

    /**
     * 删除
     * 需求：删除用户之前 先鉴权 管理员才可以删除普通用户
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
       /* String headToken = request.getHeader("Authorization");//前端通过head传入的token
        //格式化校验
        if(StringUtils.isEmpty(headToken) || !headToken.startsWith("Bearer ")){
            return new Result(false, StatusCode.ACCESSERROR, "权限不足" );
        }
        String token = headToken.substring(7);
        Claims claims = jwtUtil.parseJWT(token);
        //前后端定义的规则： Authorization ,内容为Bearer+空格+token
        if(claims == null || !claims.get("roles").equals("admin")){
            return new Result(false, StatusCode.ACCESSERROR, "权限不足" );
        }*/
        Claims claims = (Claims) request.getAttribute("admin_claims");
        if(claims == null || !claims.get("roles").equals("admin")){
            return new Result(false, StatusCode.ACCESSERROR, "权限不足" );
        }
        userService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 发送短信验证码
     */
    @RequestMapping(value = "/sendsms/{mobile}", method = RequestMethod.POST)
    public Result sendsms(@PathVariable String mobile) {
        userService.sendsms(mobile);
        return new Result(true, StatusCode.OK, "发送短信验证码成功");
    }

    /**
     * 用户注册-保存数据到数据库
     */
    @RequestMapping(value = "/register/{code}", method = RequestMethod.POST)
    public Result register(@PathVariable String code, @RequestBody User user) {
        userService.register(code, user);
        return new Result(true, StatusCode.OK, "用户注册成功");
    }


    /**
     * 登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody Map<String, String> map) {
        User user = userService.login(map.get("mobile"), map.get("password"));
        if (user != null) {
            //账号和密码验证通过后 签发token
            String token = jwtUtil.createJWT(user.getId(), user.getNickname(), "user");
            //定一个map存入token和admin信息
            Map<String,Object> rsMap = new HashMap<>();
            rsMap.put("token",token);//key需要跟前端商量
            rsMap.put("user",user);//返回给前端的用户信息
            //验证码账号和密码成功后 为此用户创建token
            return new Result(true, StatusCode.OK, "登录成功", rsMap);
        }
        return new Result(false, StatusCode.LOGINERROR, "登录失败");
    }


    /**
     *  变更粉丝数  加 减
     * @param userid
     * @param x 1 -1
     */
    @RequestMapping(value="/incfans/{userid}/{x}",method= RequestMethod.POST)
    public void incFanscount(@PathVariable String userid,@PathVariable int x){
        userService.incFanscount(userid,x);
    }


    /**
     *  增加关注数
     * @param userid
     * @param x
     */
    @RequestMapping(value="/incfollow/{userid}/{x}",method= RequestMethod.POST)
    public void incFollowcount(@PathVariable String userid,@PathVariable int x){
        userService.incFollowcount(userid,x);
    }
}
