package com.atguigu.gmall.usermanage.controller;

import com.atguigu.gmall.bean.user.UserInfo;
import com.atguigu.gmall.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Autowired
    private UserInfoService us;

    /**
     * 按ID查询
     */
    @GetMapping("{id}")
    public UserInfo getById(@PathVariable("id") String id){
        return us.selectById(id);
    }

    /**
     * 查询所有
     */
    @GetMapping
    public List<UserInfo> getAll() {
        return us.selectAll();
    }

    /**
     * 根据昵称查询
     *
     */
    @GetMapping("nickName")
    public List<UserInfo> getAllByNickName(@RequestParam("nickName") String nickName){
        return us.selectByNickName(nickName);
    }

    /**
     * 添加
     */
    @PostMapping
    public UserInfo addUserInfo(UserInfo userInfo){
        us.addUserInfo(userInfo);
        return userInfo;
    }

    /**
     *  根据ID更新不为null的字段
     *
     */
    @PutMapping
    public UserInfo updateUserInfo(UserInfo userInfo){
        us.updateUserInfo(userInfo);
        return userInfo;
    }

    /**
     * 根据ID删除
     *
     */
    @DeleteMapping("{id}")
    public String deleteById(@PathVariable("id") String id){
        us.deleteById(id);
        return id;
    }

    /**
     *  根据登录名和密码查询
     *
     */
    @PostMapping("login")
    public UserInfo login(UserInfo userInfo){
        return us.login(userInfo);
    }
}
