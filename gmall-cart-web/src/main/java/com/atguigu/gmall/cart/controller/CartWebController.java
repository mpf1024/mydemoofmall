package com.atguigu.gmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.annotation.LoginRequire;
import com.atguigu.gmall.bean.cart.CartInfo;
import com.atguigu.gmall.bean.sku.SkuInfo;
import com.atguigu.gmall.service.CartInfoService;
import com.atguigu.gmall.service.SkuInfoService;
import com.atguigu.gmall.util.CookieUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Controller
public class CartWebController {

    @Reference
    private CartInfoService cartInfoService;

    @Reference
    private SkuInfoService skuInfoService;

    @LoginRequire(autoRedirect = false)
    @RequestMapping("addToCart")
    public String addToCart(HttpServletRequest request, HttpServletResponse response) {
        String skuId = request.getParameter("skuId");
        String skuNum = request.getParameter("skuNum");
        String userId = (String) request.getAttribute("userId");
        if (userId == null || "".equals(userId)) {
            //未登录，标识userId 可能存在cookie 中
            userId = CookieUtil.getCookieValue(request, "my-userId", false);
            // 如果cookie 中没有userId,则新建一个userId,并放入cookie中
            if (userId == null) {
                userId = UUID.randomUUID().toString().replace("-", "");
                CookieUtil.setCookie(request, response, "my-userId", userId, 60 * 60 * 24 * 7, false);
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

        String tempUserId = CookieUtil.getCookieValue(request, "my-userId", false);
        List<CartInfo> cartInfoList = null;
        if (userId == null || "".equals(userId)) {
            //未登录，尝试从cookie中取
            if (tempUserId != null && !"".equals(tempUserId)) {
                cartInfoList = cartInfoService.getCartList(tempUserId);
            }
        } else {
            //已经登录
            List<CartInfo> cartTempList = null;
            if (tempUserId != null && !"".equals(tempUserId)) {
                cartTempList = cartInfoService.getCartList(tempUserId);
            }

            if (cartTempList != null && cartTempList.size() > 0) {
                // 合并购物车：cartTempList未登录购物车，根据userId 查询登录购物车数据
                cartInfoList = cartInfoService.mergeToCartList(cartTempList, userId);
                // 删除未登录购物车数据
                cartInfoService.deleteCartList(tempUserId);
            }else{
                cartInfoList = cartInfoService.getCartList(userId);
            }
        }
        request.setAttribute("cartInfoList", cartInfoList);

        return "cartList";
    }

    @RequestMapping("/checkCart")
    @LoginRequire(autoRedirect = false)
    @ResponseBody
    public void checkCart(HttpServletRequest request, HttpServletResponse response){
        // 调用服务层
        String isChecked = request.getParameter("isChecked");
        String skuId = request.getParameter("skuId");
        // 获取用户Id
        String userId = (String) request.getAttribute("userId");

        // 判断用户的状态！
        if (userId==null) {
            // 登录状态
            userId=CookieUtil.getCookieValue(request,"my-userId",false);
        }
        cartInfoService.checkCart(isChecked, skuId, userId);
    }

}
