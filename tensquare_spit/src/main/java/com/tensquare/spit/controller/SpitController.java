package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 吐槽微服务控制层
 */
@RestController
@RequestMapping("/spit")
@CrossOrigin
public class SpitController {
    @Autowired
    private SpitService spitService;

    /**
     * 增
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Spit spit) {
        spitService.save(spit);
        return new Result(true, StatusCode.OK, "吐槽发布成功");
    }



    /**
     * 删除
     */
    @RequestMapping(value = "/{spitId}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String spitId) {
        spitService.deleteById(spitId);
        return new Result(true, StatusCode.OK, "吐槽删除成功");
    }



    /**
     * 改
     */
    @RequestMapping(value = "/{spitId}",method = RequestMethod.PUT)
    public Result updateById(@PathVariable String spitId,@RequestBody Spit spit) {
        spitService.updateById(spitId,spit);
        return new Result(true, StatusCode.OK, "吐槽修改成功");
    }


    /**
     * 查询所有吐槽信息
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        List<Spit> spitList = spitService.findAll();
        return new Result(true, StatusCode.OK, "查询吐槽所有文档成功",spitList);
    }


    /**
     * 根据id查询吐槽
     */
    @RequestMapping(value = "/{spitId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String spitId) {
        Spit spit = spitService.findById(spitId);
        return new Result(true, StatusCode.OK, "根据id查询吐槽文档成功",spit);
    }

    /**
     * 根据上级ID查询吐槽数据（分页）
     */
    @RequestMapping(value = "/comment/{parentid}/{page}/{size}",method = RequestMethod.GET)
    public Result comment(@PathVariable String parentid,@PathVariable int page,@PathVariable int size) {
        Page<Spit> spitPage = spitService.comment(parentid,page,size);
        return new Result(true, StatusCode.OK, "根据上级ID查询吐槽数据成功",new PageResult<>(spitPage.getTotalElements(),spitPage.getContent()));
    }


    /**
     * 吐槽点赞
     *
     * 控制层一般都是写的输入参数  输出结果
     */
    @RequestMapping(value = "/thumbup/{spitId}",method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String spitId) {
        int rs = spitService.thumbup(spitId);
        if(rs == 1){
            return new Result(false, StatusCode.OK, "已经点赞");
        }
        return new Result(true, StatusCode.OK, "吐槽点赞成功");
    }

}
