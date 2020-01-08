package com.atguigu.gmall.cartservice.mapper;

import com.atguigu.gmall.bean.cart.CartInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CartInfoMapper extends Mapper<CartInfo> {
    List<CartInfo> selectCartListWithCurPrice(String userId);
}
