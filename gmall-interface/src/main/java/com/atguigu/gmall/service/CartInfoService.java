package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.cart.CartInfo;

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
     * 合并未登录购物车到用户购物车,如果有相同的商品，只合并选中状态，不合并数量
     * 合并完后删除未登录购物车 返回合并后的购物车列表
     * @param tempUserId - 未登录的临时userId
     * @param userId -  已经登录的用户ID
     */
    List<CartInfo> mergeToCartList(String tempUserId, String userId);

    /**
     * 删除购物车商品(主要为未登录购物车)
     * @param tempUserId 用户ID
     */
    void deleteCartList(String tempUserId);

    /**
     * 更新勾选状态，只更新缓存
     */
    void checkCart(String isChecked, String skuId, String userId);

    /**
     * 得到选中的购物车列表
     */
    List<CartInfo> getCartCheckedList(String userId);
}
