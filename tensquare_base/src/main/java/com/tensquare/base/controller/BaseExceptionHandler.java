package com.tensquare.base.controller;

import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 公共异常处理类
 */
@RestControllerAdvice //负责拦截异常请求
//@ControllerAdvice + @ResponseBody == @RestControllerAdvice
public class BaseExceptionHandler {
    //拦截异常请求后 返回错误结果给前端
    @ExceptionHandler(Exception.class)
    public Result error(Exception e){
        return new Result(false, StatusCode.ERROR,e.getMessage());
    }
}
