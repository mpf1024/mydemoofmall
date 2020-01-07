package com.atguigu.gmall.interceptor;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.annotation.LoginRequire;
import com.atguigu.gmall.util.CookieUtil;
import com.atguigu.gmall.util.HttpClientUtil;
import com.atguigu.gmall.util.WebConst;
import io.jsonwebtoken.impl.Base64UrlCodec;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws IOException {

        String token = request.getParameter("newToken");
        System.out.println(request.getRequestURL().toString() + "..........AuthInterceptor................");
        if(token!=null){
            //从登录页面过来的，将新的token放入cookie中
            CookieUtil.setCookie(request,response,"token",token, WebConst.COOKIE_MAXAGE,false);
        }else
        {   //非登录页面过来，从cookie中取
            token = CookieUtil.getCookieValue(request,"token",false);
        }

        if(token != null){
            Map map = getUserInfoByToken(token);//获取token中的user信息
            Object nickName = map.get("nickName");
            request.setAttribute("nickName",nickName);
        }

        //获取注解LoginRequire
        HandlerMethod handlerMethod = (HandlerMethod) o;
        LoginRequire methodAnnotation = handlerMethod.getMethodAnnotation(LoginRequire.class);
        if(methodAnnotation!=null){
            String salt = request.getHeader("X-forwarded-for");
            String url = WebConst.VERIFY_ADDRESS + "?token=" + token + "&currentIp=" + salt;
            String result = HttpClientUtil.doGet(url);//验证登录状态
            if("success".equals(result)){
                //已登录且没有过期
                Map map = getUserInfoByToken(token);//获取token中的user信息
                Object nickName = map.get("userId");
                request.setAttribute("userId",nickName);
            }else{
                if(methodAnnotation.autoRedirect()){
                    //重定向到登录页面
                    String requestUrl = request.getRequestURL().toString();
                    String encodeURL = URLEncoder.encode(requestUrl, "UTF-8");
                    response.sendRedirect(WebConst.LOGIN_ADDRESS + "?originUrl=" + encodeURL);
                    return  false;
                }
            }

        }
        return true;
    }

    private Map getUserInfoByToken(String token) {
        String tokenUserInfo = StringUtils.substringBetween(token, ".");
        Base64UrlCodec base64UrlCodec = new Base64UrlCodec();
        byte[] tokenBytes = base64UrlCodec.decode(tokenUserInfo);
        String tokenJson = new String(tokenBytes);
        return JSON.parseObject(tokenJson, Map.class);
    }
}
