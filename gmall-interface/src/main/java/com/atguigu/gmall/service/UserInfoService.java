package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.UserInfo;

import java.util.List;

public interface UserInfoService {
    /**
     * @param id 查询ID
     * @return 指定ID的user info
     */
    UserInfo selectById(String id);

    /**
     * @return 所有结果
     */
    List<UserInfo> selectAll();

    /**
     * 按昵称查询
     *
     * @param nickName 昵称
     * @return 结果
     */
    List<UserInfo> selectByNickName(String nickName);

    /**
     * @param userInfo 添加的user信息
     */
    void addUserInfo(UserInfo userInfo);

    /**
     * @param userInfo 更新的user信息
     */
    void updateUserInfo(UserInfo userInfo);

    /**
     * @param id 要删除的ID
     */
    void deleteById(String id);

    /**
     * 查询登录
     *
     * @param loginName 登录名
     * @param password  密码
     * @return 若存在用户，返回对应的用户，若不存在，返回null
     */
    UserInfo login(String loginName, String password);

}
