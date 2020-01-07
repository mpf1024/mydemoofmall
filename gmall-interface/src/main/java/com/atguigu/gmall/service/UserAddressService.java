package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.user.UserAddress;

import java.util.List;

public interface UserAddressService {

    /**
     *
     * @param userId 用户ID
     * @return  指定用户的所有地址信息
     */
    List<UserAddress> getUserAddressByUserId(String userId);
}
