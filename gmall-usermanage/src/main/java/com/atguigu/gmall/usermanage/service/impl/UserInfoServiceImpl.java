package com.atguigu.gmall.usermanage.service.impl;

import com.atguigu.gmall.bean.UserInfo;
import com.atguigu.gmall.service.UserInfoService;
import com.atguigu.gmall.usermanage.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper uim;

    @Override
    public UserInfo selectById(String id) {
        return uim.selectByPrimaryKey(id);
    }

    @Override
    public List<UserInfo> selectAll() {
        return uim.selectAll();
    }

    @Override
    public List<UserInfo> selectByNickName(String nickName) {
        Example example = new Example(UserInfo.class);
        example.createCriteria().andLike("nickName","%" + nickName + "%");
        return uim.selectByExample(example);
    }

    @Override
    public void addUserInfo(UserInfo userInfo) {
        uim.insert(userInfo);
    }

    @Override
    public void updateUserInfo(UserInfo userInfo) {
        uim.updateByPrimaryKeySelective(userInfo);
    }

    @Override
    public void deleteById(String id) {
        uim.deleteByPrimaryKey(id);
    }

    @Override
    public UserInfo login(String loginName, String password) {
        Example example = new Example(UserInfo.class);
        example.createCriteria().andEqualTo("loginName",loginName)
                .andEqualTo("passwd",password);
        return uim.selectOneByExample(example);
    }
}
