package com.tensquare.user.service;

import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.tensquare.user.pojo.Admin;
import entity.Result;
import entity.StatusCode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import util.IdWorker;

import com.tensquare.user.dao.UserDao;
import com.tensquare.user.pojo.User;

/**
 * 服务层
 *
 * @author Administrator
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询全部列表
     *
     * @return
     */
    public List<User> findAll() {
        return userDao.findAll();
    }


    /**
     * 条件查询+分页
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<User> findSearch(Map whereMap, int page, int size) {
        Specification<User> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return userDao.findAll(specification, pageRequest);
    }


    /**
     * 条件查询
     *
     * @param whereMap
     * @return
     */
    public List<User> findSearch(Map whereMap) {
        Specification<User> specification = createSpecification(whereMap);
        return userDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public User findById(String id) {
        return userDao.findById(id).get();
    }

    @Autowired
    private BCryptPasswordEncoder bcryt;

    /**
     * 增加
     *
     * @param user
     */
    public void add(User user) {
        user.setPassword(bcryt.encode(user.getPassword()));
        user.setId(idWorker.nextId() + "");
        userDao.save(user);
    }

    /**
     * 修改
     *
     * @param user
     */
    public void update(User user) {
        userDao.save(user);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        userDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<User> createSpecification(Map searchMap) {

        return new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                // 手机号码
                if (searchMap.get("mobile") != null && !"".equals(searchMap.get("mobile"))) {
                    predicateList.add(cb.like(root.get("mobile").as(String.class), "%" + (String) searchMap.get("mobile") + "%"));
                }
                // 密码
                if (searchMap.get("password") != null && !"".equals(searchMap.get("password"))) {
                    predicateList.add(cb.like(root.get("password").as(String.class), "%" + (String) searchMap.get("password") + "%"));
                }
                // 昵称
                if (searchMap.get("nickname") != null && !"".equals(searchMap.get("nickname"))) {
                    predicateList.add(cb.like(root.get("nickname").as(String.class), "%" + (String) searchMap.get("nickname") + "%"));
                }
                // 性别
                if (searchMap.get("sex") != null && !"".equals(searchMap.get("sex"))) {
                    predicateList.add(cb.like(root.get("sex").as(String.class), "%" + (String) searchMap.get("sex") + "%"));
                }
                // 头像
                if (searchMap.get("avatar") != null && !"".equals(searchMap.get("avatar"))) {
                    predicateList.add(cb.like(root.get("avatar").as(String.class), "%" + (String) searchMap.get("avatar") + "%"));
                }
                // E-Mail
                if (searchMap.get("email") != null && !"".equals(searchMap.get("email"))) {
                    predicateList.add(cb.like(root.get("email").as(String.class), "%" + (String) searchMap.get("email") + "%"));
                }
                // 兴趣
                if (searchMap.get("interest") != null && !"".equals(searchMap.get("interest"))) {
                    predicateList.add(cb.like(root.get("interest").as(String.class), "%" + (String) searchMap.get("interest") + "%"));
                }
                // 个性
                if (searchMap.get("personality") != null && !"".equals(searchMap.get("personality"))) {
                    predicateList.add(cb.like(root.get("personality").as(String.class), "%" + (String) searchMap.get("personality") + "%"));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 用户注册-发送短信验证码
     *
     * @param mobile
     */
    public void sendsms(String mobile) {
        //1.生成验证码 规则：6位 100000-999999
        int min = 100000;
        int max = 999999;
        //随机产生验证码
        Random random = new Random();
        int randNum = random.nextInt(max);//有可能不足6位
        //不足6位 补全6位
        if (randNum < min) {
            randNum = randNum + min;
        }
        System.out.println("手机号码："+mobile+"验证码："+randNum);
        //获取最终的验证码 randNum  发送的流程 通短信微服务单独发送
        //1.将验证码存入redis 10分钟
        redisTemplate.opsForValue().set("code_"+mobile,randNum+"",10, TimeUnit.MINUTES);

        //发送短信需要什么数据？ 手机号码 验证码
        Map<String,String> map = new HashMap<>();
        map.put("mobile",mobile);
        map.put("code",randNum+"");
        //2.将验证码发送短信消息 存入RabbitMQ消息队列中
        rabbitTemplate.convertAndSend("sms",map);

    }

    /**
     * 用户注册
     * @param code
     * @param user
     */
    public void register(String code, User user) {
        //1.判断验证码是否一致
        String mobile = user.getMobile();
        String redisCode = (String)redisTemplate.opsForValue().get("code_" + mobile);
        if(StringUtils.isEmpty(redisCode) || StringUtils.isEmpty(code) || !code.equals(redisCode)){
            throw new RuntimeException("验证码错误，请重新获取");
        }
        //设置主键id
        user.setId(idWorker.nextId()+"");
        ///设置默认值
        user.setFollowcount(0);//关注数
        user.setFanscount(0);//粉丝数
        user.setOnline(0L);//在线时长
        user.setRegdate(new Date());//注册日期
        user.setUpdatedate(new Date());//更新日期
        user.setLastdate(new Date());//最后登陆日期
        userDao.save(user);
        System.out.println("用户注册成功了");
        //删除redis中验证码
        redisTemplate.delete("code_" + mobile);

    }


    /**
     * 登录
     * @param mobile
     * @param password
     * @return
     */
    public User login(String mobile, String password) {
        //select *from t_user where loginname =xx and password =yyy
        //根据登录用户名查询账号是否存在  命名查询语句
        User user = userDao.findByMobile(mobile);
        if (user == null) {
            return null;
        }
        boolean matches = bcryt.matches(password, user.getPassword());
        if (matches) {
            return user;
        }
        return null;
    }

    /**
     * 添加或减少粉丝数 公共代码
     * @param userid
     * @param x
     */
    @Transactional
    public void incFanscount(String userid, int x) {
        userDao.incFanscount(userid,x);
    }

    /**
     * 添加或减少关注数 公共代码
     * @param userid
     * @param x
     */
    @Transactional
    public void incFollowcount(String userid, int x) {
        userDao.incFollowcount(userid,x);
    }
}
