package com.tensquare.web;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 前台网关过滤器
 * 1.转发请求
 * 2.将token转发到对应的微服务中
 *
 *
 * 测试注意：
 * 先启动eureka server
 * 再启动问答微服务
 * 最后启动前台网关
 */
@Component
public class WebFilter extends ZuulFilter {
    /**
     * filterType
     * pre	：可以在请求被路由之前调用
     - route：在路由请求时候被调用
     - post：在route和error过滤器之后被调用
     - error：处理请求时发生错误时被调用

     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * filterorder
     * 过滤器优先级 数字越大优先级别越低
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 是否执行当前过滤器（开关） true:开启 false:关闭
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 具体的功能处理的
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        System.out.println("进入了过滤器。。。。");
        //1.获取网关中请求容器对象
        RequestContext currentContext = RequestContext.getCurrentContext();
        //2通过网关容器对象获取httpservletrequest对象
        HttpServletRequest request = currentContext.getRequest();
        //3根据requset对象获取请求头中的Authorization
        String authorization = (String)request.getHeader("Authorization");
        if(!StringUtils.isEmpty(authorization)){
            //addZuulRequestHeader:请求转发 放入request，将数据传给后续的微服务
            //addZuulResponseHeader:请求返回给浏览器
            currentContext.addZuulRequestHeader("Authorization",authorization);
        }
        //返回成功
        return null;
    }
}
