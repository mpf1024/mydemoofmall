package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.order.OrderInfo;

public interface OrderInfoService {
    /**
     * 保存订单并返回保存后的ID
     */
    String  saveOrder(OrderInfo orderInfo);

    /**
     * 生成流水号，用于防止表单重复提交
     */
    String getTradeNo(String userId);

    /**
     * 验证流水号
     */
    boolean checkTradeCode(String userId,String tradeCodeNo);

    /**
     *  删除流水号
     */
    void deleteTradeCode(String userId);

    /**
     * 验证库存
     */
    boolean checkStock(String skuId, Integer skuNum);

    /**
     * 获取订单信息
     */
    OrderInfo getOrderInfo(String orderId);
}
