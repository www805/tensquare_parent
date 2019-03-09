package com.tensquare.qa.client;

import com.tensquare.qa.client.impl.LabelClientImpl;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 需求：问答微服务调用基础微服务 （根据标签id查询标签信息）
 * 1.需要引入feign依赖
 * 2.在启动类中加入2个注解 启动远程访问&启动feign组件
 * 3.开发接口（客户端）
 * 4.在接口上加注解指定被调用的微服务名称  （服务名不能有下划线）
 * 5.修改客户端访问服务端的接口中方法的请求地址（被调用服务端全路径）
 * 6.@PathVariable参数必须设置别名 不能调用会失败
 * 7.在客户端 LabelController中加入接口 提供对外访问 接口中需要调用基础微服务
 *
 * 将问答微服务和基础微服务注册到注册中心
 * 1.引入eureka-client依赖
 * 2.加入客户端配置 重点注意ip和port跟服务端要一致
 * 3.在启动类上加上注解 开发eureka-client 启动中会将当前微服务注册到注册中心，提供给其它微服务进行调用
 * fallback:调用基础微服务失败后，回调指定的实现类返回错误信息
 */
@FeignClient(value = "tensquare-base",fallback = LabelClientImpl.class)
public interface LabelClient {
    //加入被调用的接口中方法
    @RequestMapping(value = "/label/{labelId}", method = RequestMethod.GET)
    public Result findById(@PathVariable("labelId") String labelId);
}
