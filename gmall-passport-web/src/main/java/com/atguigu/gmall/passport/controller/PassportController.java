package com.atguigu.gmall.passport.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.user.UserInfo;
import com.atguigu.gmall.passport.util.JwtUtil;
import com.atguigu.gmall.service.UserInfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PassportController {

    @Value("${token.key}")
    String signKey;

    @Reference
    private UserInfoService userInfoService;


    @RequestMapping("index")
    public String index(HttpServletRequest request) {
        String originUrl = request.getParameter("originUrl");
        // 保存上
        request.setAttribute("originUrl", originUrl);
        return "index";
    }

    @RequestMapping("login")
    @ResponseBody
    public String login(UserInfo userInfo, HttpServletRequest request) {
        // 取得ip地址 访问时要用passport.gmall.com,才会走nginx中设置请求头
        String remoteAddr = request.getHeader("X-forwarded-for");
        UserInfo user = userInfoService.login(userInfo);
        String token = "fail";
        if (user != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", user.getId());
            map.put("nickName", user.getNickName());
            token = JwtUtil.encode(signKey, map, remoteAddr);
        }
        return token;
    }

    @RequestMapping("verify")
    @ResponseBody
    public String verify(HttpServletRequest request){
        String token = request.getParameter("token");//获取token
        String currentIp = request.getParameter("currentIp");
        // 检查token
        Map<String, Object> map = JwtUtil.decode(token, signKey, currentIp);
        if (map!=null){
            // 检查redis信息
            String userId = (String) map.get("userId");
            UserInfo userInfo = userInfoService.verify(userId);
            if (userInfo!=null){
                return "success";
            }
        }
        return "fail";
    }
}
