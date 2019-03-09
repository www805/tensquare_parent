package com.tensquare.friend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import util.IdWorker;
import util.JwtUtil;

/**
 * 交友微服务启动类
 */
@SpringBootApplication
@EnableEurekaClient //在微服务启动的时候将当前微服务注册到注册中心
//通过交友微服务 调用用户微服务
@EnableDiscoveryClient //启动远程访问
@EnableFeignClients  //启动服务调用组件
public class FriendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FriendApplication.class);
    }

    //jwtutil
    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil();
    }

    //idwork
    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }

}
