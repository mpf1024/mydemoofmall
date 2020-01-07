package com.atguigu.gmall.service;

public interface CartInfoService {
    /**
     * 添加到购物车
     * @param skuId 商品ID
     * @param userId 用户ID
     * @param skuNum 数量
     */
    void  addToCart(String skuId,String userId,Integer skuNum);
}
