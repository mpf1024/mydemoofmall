package com.atguigu.gmall.usermanage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.user.UserInfo;
import com.atguigu.gmall.config.RedisUtil;
import com.atguigu.gmall.service.UserInfoService;
import com.atguigu.gmall.usermanage.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper uim;

    @Autowired
    private RedisUtil redisUtil;

    private String userKey_prefix="user:";
    private String userinfoKey_suffix=":info";
    private int userKey_timeOut=60*60*24;

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
    public UserInfo login(UserInfo userInfo) {
        String password = DigestUtils.md5DigestAsHex(userInfo.getPasswd().getBytes());
        UserInfo user = new UserInfo();
        user.setLoginName(userInfo.getLoginName());
        user.setPasswd(password);
        UserInfo loginUser = uim.selectOne(user);
        if(loginUser != null){
            Jedis jedis = redisUtil.getJedis();
            jedis.setex(userKey_prefix+loginUser.getId()+userinfoKey_suffix,userKey_timeOut,
                        JSON.toJSONString(loginUser));
            jedis.close();
            return loginUser;
        }
        return null;
    }

    @Override
    public UserInfo verify(String userId) {
        String userKey = userKey_prefix+userId+userinfoKey_suffix;
        Jedis jedis = redisUtil.getJedis();
        String userJson = jedis.get(userKey);
        if(userJson!=null){
            //说明已经登录过且没过期
            UserInfo userInfo = JSON.parseObject(userJson, UserInfo.class);
            // 延长时效
            jedis.expire(userKey,userKey_timeOut);
            return userInfo;
        }

        return null;
    }
}
