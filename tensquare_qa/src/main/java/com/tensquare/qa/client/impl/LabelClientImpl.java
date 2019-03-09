package com.tensquare.qa.client.impl;

import com.tensquare.qa.client.LabelClient;
import entity.Result;
import entity.StatusCode;
import org.springframework.stereotype.Component;

/**
 * 熔断器实现类
 */
@Component
public class LabelClientImpl implements LabelClient{
    @Override
    public Result findById(String labelId) {
        //1.返回错误信息
        //2.记录错误日志
        //3.尝试从缓存中获取信息，如果获取成功则返回标签信息，如果失败则直接返回错误信息
        //4....
        return new Result(false, StatusCode.REMOTEERROR,"进入了熔断器，服务调用失败了");
    }
}
