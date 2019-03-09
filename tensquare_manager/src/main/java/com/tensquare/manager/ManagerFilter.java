package com.tensquare.manager;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 后台网关微服务
 * 1.解决跨域问题（不需要鉴权）
 * 2登录请求处理（不需要鉴权）
 * 3.鉴权(将token转发到后台微服务)
 *
 */
@Component
public class ManagerFilter extends ZuulFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        RequestContext currentContext = RequestContext.getCurrentContext();//网关容器对象
        HttpServletRequest request = currentContext.getRequest();
        String authorization = request.getHeader("Authorization");//token
        //1.解决跨域问题（不需要鉴权）  跨域分为两次请求 第一个词OPTIONS
        if(request.getMethod().equals("OPTIONS")){
            //放行执行成功了
            return  null;
        }
        // 2.登录请求处理（不需要鉴权）
        if(request.getRequestURL().indexOf("/login")>0){
            //放行执行成功了
            return  null;
        }
        //3.将token转发到后台微服务（鉴权）
        if(!StringUtils.isEmpty(authorization) && authorization.startsWith("Bearer ")){
            String token = authorization.substring(7);
            Claims claims = jwtUtil.parseJWT(token);
            //前后端定义的规则： Authorization ,内容为Bearer+空格+token
            if(claims != null){
                if(claims.get("roles").equals("admin")){
                    //管理员用户
                    currentContext.addZuulRequestHeader("Authorization",authorization);
                    //在网关中鉴权已经成功了
                    return  null;
                }
            }
        }

        //鉴权失败 权限不足 返回码 401  设置编码方式
        currentContext.setSendZuulResponse(false);//流程直接结束
        currentContext.setResponseBody("权限不足");
        currentContext.getResponse().setContentType("text/html;charset=UTF-8");//设置数据类型
        currentContext.setResponseStatusCode(401);//代表权限不足
        return null;
    }
}
