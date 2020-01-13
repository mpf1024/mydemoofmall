package com.atguigu.gmall.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.annotation.LoginRequire;
import com.atguigu.gmall.bean.cart.CartInfo;
import com.atguigu.gmall.bean.order.OrderDetail;
import com.atguigu.gmall.bean.order.OrderInfo;
import com.atguigu.gmall.bean.user.UserAddress;
import com.atguigu.gmall.enums.OrderStatus;
import com.atguigu.gmall.enums.ProcessStatus;
import com.atguigu.gmall.service.CartInfoService;
import com.atguigu.gmall.service.OrderInfoService;
import com.atguigu.gmall.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {
    @Reference
    private UserInfoService userInfoService;

    @Reference
    private CartInfoService cartInfoService;
    @Autowired
    private OrderInfoService orderInfoService;

    @RequestMapping("/trade")
    @LoginRequire
    public  String tradeInit(HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        // 得到选中的购物车列表
        List<CartInfo> cartCheckedList = cartInfoService.getCartCheckedList(userId);
        // 收货人地址
        List<UserAddress> userAddressList = userInfoService.getUserAddressByUserId(userId);
        request.setAttribute("userAddressList",userAddressList);
        // 订单信息集合
        List<OrderDetail> orderDetailList=new ArrayList<>(cartCheckedList.size());
        for (CartInfo cartInfo : cartCheckedList) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setSkuId(cartInfo.getSkuId());
            orderDetail.setSkuName(cartInfo.getSkuName());
            orderDetail.setImgUrl(cartInfo.getImgUrl());
            orderDetail.setSkuNum(cartInfo.getSkuNum());
            orderDetail.setOrderPrice(cartInfo.getSkuPrice());
            orderDetailList.add(orderDetail);
        }
        request.setAttribute("orderDetailList",orderDetailList);
        OrderInfo orderInfo=new OrderInfo();
        orderInfo.setOrderDetailList(orderDetailList);
        orderInfo.sumTotalAmount();
        request.setAttribute("totalAmount",orderInfo.getTotalAmount());

        // 获取TradeCode号
        String tradeNo = orderInfoService.getTradeNo(userId);
        request.setAttribute("tradeCode",tradeNo);

        return  "trade";
    }

    @RequestMapping(value = "submitOrder",method = RequestMethod.POST)
    @LoginRequire
    public String submitOrder(OrderInfo orderInfo,HttpServletRequest request){

        String userId = (String) request.getAttribute("userId");
        // 检查tradeCode
        String tradeNo = request.getParameter("tradeNo");
        boolean flag = orderInfoService.checkTradeCode(userId, tradeNo);
        if (!flag){
            request.setAttribute("errMsg","该页面已失效，请重新结算!");
            return "tradeFail";
        }
        // 删除tradeNo
        orderInfoService.deleteTradeCode(userId);

        //校验，验价
        List<OrderDetail> orderDetailList = orderInfo.getOrderDetailList();

        for (OrderDetail orderDetail : orderDetailList) {
            //验证库存
            boolean result = orderInfoService.checkStock(orderDetail.getSkuId(), orderDetail.getSkuNum());
            if (!result){
                request.setAttribute("errMsg",orderDetail.getSkuName() + "的库存不足，请重新下单！");
                return "tradeFail";
            }
            //验证价格...
        }

        // 初始化参数
        orderInfo.setOrderStatus(OrderStatus.UNPAID);
        orderInfo.setProcessStatus(ProcessStatus.UNPAID);
        orderInfo.sumTotalAmount();
        orderInfo.setUserId(userId);
        // 保存
        String orderId = orderInfoService.saveOrder(orderInfo);

        // 重定向
        return "redirect://payment.gmall.com/index?orderId="+orderId;
    }

    @RequestMapping("/list")
    public String list(){
        return "list";
    }
}
