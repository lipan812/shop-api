package com.fh.interceptors;


import com.alibaba.fastjson.JSONObject;
import com.fh.annotation.Ignore;
import com.fh.common.SystemConstant;
import com.fh.member.model.Member;
import com.fh.util.JwtUtil;
import com.fh.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.net.URLDecoder;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //处理客户端传过来的自定义头信息
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "x-auth,mtoken,content-type");
        // 处理客户端发过来put,delete
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "PUT,POST,DELETE,GET");

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //判断该方法是否需要拦截   如果有@Ignore注解就放行
        if (method.isAnnotationPresent(Ignore.class)){
            return true;
        }
        //获取请求头里token
        String token = request.getHeader("x-auth");
        if (StringUtils.isEmpty(token)){
            throw new LoginException();
        }
        //验证token是否失效
        boolean exist = RedisUtil.exist(SystemConstant.TOKEN_KEY+token);
        if (!exist){
            //如果token失效
            throw new LoginException();
        }/*else {
            token = RedisUtil.get(token);
        }*/

        //验证token
        boolean res = JwtUtil.verify(token);
        if (res){
            String userString = JwtUtil.getUser(token);
            String jsonUser = URLDecoder.decode(userString, "utf-8");
            Member member = JSONObject.parseObject(jsonUser, Member.class);
            request.getSession().setAttribute(SystemConstant.SESSION_KEY,member);
            request.getSession().setAttribute(SystemConstant.TOKEN_KEY,token);

            //给token续命
            RedisUtil.setEx(token,token, SystemConstant.TOKEN_EXPIRE);
            System.out.println(member.toString());
            System.out.println("==========================");
        }else {
            throw new LoginException();
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
