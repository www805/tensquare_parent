package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import util.IdWorker;

import java.util.Date;
import java.util.List;

/**
 * 吐槽微服务业务逻辑处理类
 */
@Service
public class SpitService {

    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    //mongodb模板
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 增
     */
    public void save(Spit spit) {
        //设置主键id 通过idwork获取
        spit.set_id(idWorker.nextId()+"");
        //初始化赋值
        spit.setPublishtime(new Date());//发布日期
        spit.setVisits(0);//浏览量
        spit.setShare(0);//分享数
        spit.setThumbup(0);//点赞数
        spit.setComment(0);//回复数
        spit.setState("1");//状态
        //针对某个吐槽进行再次吐槽（评论）
        if(!StringUtils.isEmpty(spit.getParentid())){
            //再次吐槽 需要更新被吐槽集合中回复数+1
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));//拼接查询条件
            Update update =new Update();
            update.inc("comment",1);//针对某一列自增 每次+1
            mongoTemplate.updateFirst(query,update,"spit");
        }
        spitDao.save(spit);
    }

    /**
     * 删除
     */
    public void deleteById(String spitId) {
        spitDao.deleteById(spitId);
    }

    /**
     * 改
     */
    public void updateById(String spitId, Spit spit) {
        spit.set_id(spitId);//以路径中id为准
        spitDao.save(spit);//保存和修改
    }

    /**
     * 查询所有吐槽信息
     */
    public List<Spit> findAll() {
        return spitDao.findAll();
    }

    /**
     * 根据id查询吐槽
     */
    public Spit findById(String spitId) {
        return spitDao.findById(spitId).get();
    }

    /**
     * 根据上级ID查询吐槽数据（分页）
     */
    public Page<Spit> comment(String parentid, int page, int size) {
        //拼接分页对象参数
        Pageable pageable = PageRequest.of(page-1,size);
        //命名查询语句
        return spitDao.findByParentid(parentid,pageable);
    }



    /**
     * 吐槽点赞
     * @param spitId
     */
    /*public void thumbup(String spitId) {
        //先根据id查询吐槽对象
        //setthumbup(+1)
        //save
        //效率不高  查询后更新
        //sql只需要 update table set xx=1 where id=yyy

        //Query query: 查询条件封装
        // Update update：需要更新的列
        // String collectionName：需要操作的集合名称
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(spitId));//拼接查询条件
        Update update =new Update();
        update.inc("thumbup",1);//针对某一列自增 每次+1
        mongoTemplate.updateFirst(query,update,"spit");

    }*/


    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 吐槽点赞
     * @param spitId
     */
    public int thumbup(String spitId) {
        //为了区分用户  获取用户id
        String userid = "123456"; //jwt
        //伪代码
        //1.从redis中查询当前用户是否已经对吐槽进行点赞
        String isThumbup= (String)redisTemplate.opsForValue().get("thumbup_"+userid+"_" + spitId);
        if(!StringUtils.isEmpty(isThumbup)){
            //2.如果已经点赞 则直接返回错误信息
            return 1; //1：已经点赞了 0：没有点赞
        }
        //3.如果未点赞 则执行后续点赞操作
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(spitId));//拼接查询条件
        Update update =new Update();
        update.inc("thumbup",1);//针对某一列自增 每次+1
        mongoTemplate.updateFirst(query,update,"spit");
        //4.点赞成功后 往redis中插入点赞状态
        redisTemplate.opsForValue().set("thumbup_"+userid+"_" +spitId,"1");
        return 0;

    }
}
