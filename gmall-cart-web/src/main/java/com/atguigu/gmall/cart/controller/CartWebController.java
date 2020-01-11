package com.atguigu.gmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.annotation.LoginRequire;
import com.atguigu.gmall.bean.cart.CartInfo;
import com.atguigu.gmall.service.CartInfoService;
import com.atguigu.gmall.service.SkuInfoService;
import com.atguigu.gmall.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Controller
public class CartWebController {

    @Autowired
    private CartInfoService cartInfoService;

    @Reference
    private SkuInfoService skuInfoService;

    private static final String TEMP_USERID_COOKIE_KEY = "my-userId";

    @LoginRequire(autoRedirect = false)
    @RequestMapping("addToCart")
    public String addToCart(HttpServletRequest request, HttpServletResponse response) {
        String skuId = request.getParameter("skuId");
        String skuNum = request.getParameter("skuNum");
        String userId = (String) request.getAttribute("userId");
        if (userId == null || "".equals(userId)) {
            //未登录，标识userId 可能存在cookie 中
            userId = CookieUtil.getCookieValue(request, TEMP_USERID_COOKIE_KEY, false);
            // 如果cookie 中没有userId,则新建一个userId,并放入cookie中
            if (userId == null) {
                userId = UUID.randomUUID().toString().replace("-", "");
                CookieUtil.setCookie(request, response, TEMP_USERID_COOKIE_KEY, userId, 60 * 60 * 24 * 7, false);
            }
        }
        CartInfo cartInfo = cartInfoService.addToCart(skuId, userId, Integer.parseInt(skuNum));
        request.setAttribute("cartInfo", cartInfo);
        return "success";
    }

    @LoginRequire(autoRedirect = false)
    @RequestMapping("/cartList")
    public String toCartList(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");

        String tempUserId = CookieUtil.getCookieValue(request, TEMP_USERID_COOKIE_KEY, false);
        List<CartInfo> cartInfoList = null;
        if (userId == null || "".equals(userId)) {
            //未登录，获取未登录购物车
            if (tempUserId != null && !"".equals(tempUserId)) {
                cartInfoList = cartInfoService.getCartList(tempUserId);
            }
        } else {
            //已经登录
            if (tempUserId != null && !"".equals(tempUserId)) {
                //合并购物车
                cartInfoList = cartInfoService.mergeToCartList(tempUserId,userId);
            }
        }
        request.setAttribute("cartInfoList", cartInfoList);

        return "cartList";
    }

    @RequestMapping("/checkCart")
    @LoginRequire(autoRedirect = false)
    @ResponseBody
    public void checkCart(HttpServletRequest request){
        // 调用服务层
        String isChecked = request.getParameter("isChecked");
        String skuId = request.getParameter("skuId");
        // 获取用户Id
        String userId = (String) request.getAttribute("userId");

        // 判断用户的状态！
        if (userId==null) {
            // 未登录状态
            userId=CookieUtil.getCookieValue(request,TEMP_USERID_COOKIE_KEY,false);
        }
        cartInfoService.checkCart(isChecked, skuId, userId);
    }

    @RequestMapping("/toTrade")
    @LoginRequire
    public String toTrade(HttpServletRequest request){
        // 获取userId
        String userId = (String) request.getAttribute("userId");
        // 获取cookie 中的my-userId
        String userTempId = CookieUtil.getCookieValue(request, TEMP_USERID_COOKIE_KEY, false);
        if (userTempId!=null){
            //合并
            cartInfoService.mergeToCartList(userTempId,userId);
        }
        return "redirect://trade.gmall.com/trade";
    }

}
