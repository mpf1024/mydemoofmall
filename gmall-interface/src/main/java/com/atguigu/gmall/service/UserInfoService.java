package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.user.UserInfo;

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
     * 执行登录并将UserInfo放入redis中
     * @param userInfo -
     */
    UserInfo login(UserInfo userInfo);

    /**
     * 通过userId去redis中读取对应的信息验证登录状态
     * @param userId 用户ID
     */
    UserInfo verify(String userId);
}
