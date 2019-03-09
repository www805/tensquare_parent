package com.tensquare.base.controller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 标签控制层
 */
@RestController
@RequestMapping("/label")
@CrossOrigin //解决跨域问题
@RefreshScope //自定义配置需要使用此配置进行刷新
public class LabelController {

    @Autowired
    private LabelService labelService;

    /**
     * 保存
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Label label) {
        labelService.save(label);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/{labelId}", method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String labelId) {
        labelService.deleteById(labelId);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/{labelId}", method = RequestMethod.PUT)
    public Result updateById(@RequestBody Label label, @PathVariable String labelId) {
        labelService.updateById(label, labelId);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 根据id查询标签数据
     */
    @RequestMapping(value = "/{labelId}", method = RequestMethod.GET)
    public Result findById(@PathVariable String labelId) {
        Label label = labelService.findById(labelId);
        return new Result(true, StatusCode.OK, "根据id查询成功", label);
    }


    /**
     * 查询 所有数据
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        int a = 1 / 0;
        List<Label> labelList = labelService.findAll();
        return new Result(true, StatusCode.OK, "根据id查询成功", labelList);
    }


    /**
     * 标签条件查询
     */
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public Result search(@RequestBody Map<String,String> map) {
        List<Label> labelList = labelService.findAll(map);
        return new Result(true, StatusCode.OK, "条件查询成功", labelList);
    }



    /**
     * 带分页的标签条件查询
     */
    @RequestMapping(value = "/search/{page}/{size}",method = RequestMethod.POST)
    public Result search(@PathVariable int page,@PathVariable int size,@RequestBody Map<String,String> map) {
        Page<Label> labelPage = labelService.findAll(page,size,map);
        return new Result(true, StatusCode.OK, "分页条件查询成功", new PageResult<>(labelPage.getTotalElements(),labelPage.getContent()));
    }


    /**
     * 自定义配置 自动刷新需要在类上加上注解
     */
    @Value("${sms.ip}")
    private String ip;

    @RequestMapping(value = "/ip", method = RequestMethod.GET)
    public String ip() {
        return ip;
    }


}
