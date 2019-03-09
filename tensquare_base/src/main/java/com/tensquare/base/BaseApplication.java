package com.tensquare.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import util.IdWorker;

/**
 * 基础微服务
 */
@SpringBootApplication
@EnableEurekaClient // 启动eureka客户端 将当前 微服务注册到注册中心
public class BaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class);
    }

    //启动的时候 实例化到IOC容器中
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1, 1);
    }
}
