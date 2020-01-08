package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.cart.CartInfo;
import com.atguigu.gmall.bean.sku.SkuInfo;

import java.util.List;

public interface CartInfoService {
    /**
     * 添加到购物车
     * @param skuId 商品ID
     * @param userId 用户ID
     * @param skuNum 数量
     */
    CartInfo addToCart(String skuId, String userId, Integer skuNum);

    /**
     * 获取购物车信息
     * @param userId -
     */
    List<CartInfo> getCartList(String userId);

    /**
     * 合并未登录购物车到用户购物车
     * @param cartTempList -
     * @param userId -
     */
    List<CartInfo> mergeToCartList(List<CartInfo> cartTempList, String userId);

    /**
     * 删除购物车(主要为未登录购物车)
     * @param tempUserId -
     */
    void deleteCartList(String tempUserId);

    /**
     * 更新勾选状态，只更新缓存
     */
    void checkCart(String isChecked, String skuId, String userId);
}
