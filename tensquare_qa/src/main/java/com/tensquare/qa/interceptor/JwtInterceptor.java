package com.tensquare.qa.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器实现类 请求拦截 token解析并将结果存入request对象中，
 *  1.请求先进入拦截器
 *  2.再进入控制层 就可以使用拦截器requset结果 来决定当前用户能否操作具体的功能
 */
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 进入控制层之前 先进入此方法
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String headToken = request.getHeader("Authorization");//前端通过head传入的token
        //格式化校验
        if(!StringUtils.isEmpty(headToken) && headToken.startsWith("Bearer ")){
            String token = headToken.substring(7);
            Claims claims = jwtUtil.parseJWT(token);
            //前后端定义的规则： Authorization ,内容为Bearer+空格+token
            if(claims != null){
                if(claims.get("roles").equals("admin")){
                    //管理员用户
                    request.setAttribute("admin_claims",claims);
                }
                if(claims.get("roles").equals("user")){
                    //管理员用户
                    request.setAttribute("user_claims",claims);
                }
            }
        }
        return true;
    }
}
