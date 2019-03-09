package com.tensquare.qa;

import com.tensquare.qa.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 加载拦截器配置类
 */
@Configuration
public class ApplicationConfig extends WebMvcConfigurationSupport {

	@Autowired
    private JwtInterceptor jwtInterceptor;

	//项目启动过程中添加到拦截器中
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(jwtInterceptor).
				addPathPatterns("/**").
				excludePathPatterns("/**/login"); //login不需要拦截
	}
}