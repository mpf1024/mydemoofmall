package com.atguigu.gmall.cartservice.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.cart.CartInfo;
import com.atguigu.gmall.bean.sku.SkuInfo;
import com.atguigu.gmall.cartservice.constant.CartConst;
import com.atguigu.gmall.cartservice.mapper.CartInfoMapper;
import com.atguigu.gmall.config.RedisUtil;
import com.atguigu.gmall.service.CartInfoService;
import com.atguigu.gmall.service.SkuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

@Service
public class CartInfoImpl implements CartInfoService {

    @Autowired
    private CartInfoMapper cartInfoMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Reference
    private SkuInfoService skuInfoService;

    @Override
    public void addToCart(String skuId, String userId, Integer skuNum) {
        /*
        1.  先查看数据库中是否有该商品
            true: 数量相加upd
            false: 直接添加
        2.  放入redis！
     */
        // 获取jedis
        Jedis jedis = redisUtil.getJedis();

        // 定义key user:userId:cart
        String cartKey = CartConst.USER_KEY_PREFIX + userId + CartConst.USER_CART_KEY_SUFFIX;

        CartInfo cartInfoExample = new CartInfo();
        cartInfoExample.setSkuId(skuId);
        cartInfoExample.setSkuId(userId);
        cartInfoExample.setIsChecked(null);
        CartInfo cartInfoExist = cartInfoMapper.selectOne(cartInfoExample);
        if(cartInfoExist != null){
            //已经添加过此商品
            cartInfoExist.setSkuNum(cartInfoExist.getSkuNum() + skuNum);
            //使实时价格与数据库同步
            cartInfoExist.setSkuPrice(cartInfoExist.getCartPrice());

        }else{
            //不存在
            SkuInfo skuInfo = skuInfoService.getSkuInfoById(skuId);
            cartInfoExample.setCartPrice(skuInfo.getPrice());
            cartInfoExample.setSkuPrice(skuInfo.getPrice());
            cartInfoExample.setSkuNum(skuNum);
            cartInfoExample.setSkuName(skuInfo.getSkuName());
            cartInfoExample.setImgUrl(skuInfo.getSkuDefaultImg());
            cartInfoExample.setIsChecked("1");

            cartInfoMapper.insertSelective(cartInfoExample);
            cartInfoExist = cartInfoExample;
        }

        // 更新redis 放在最后！
        String cartInfoJson = JSON.toJSONString(cartInfoExist);
        jedis.hset(cartKey,skuId, cartInfoJson);
        setCartkeyExpireTime(userId, jedis, cartKey);

        // 关闭redis！
        jedis.close();


    }

    private void setCartkeyExpireTime(String userId, Jedis jedis, String cartKey) {
        // 根据user得过期时间设置
        // 获取用户的过期时间 user:userId:info
        String userKey = CartConst.USER_KEY_PREFIX+userId+CartConst.USERINFOKEY_SUFFIX;
        // 用户key 存在，登录。
        Long expireTime = null;
        if (jedis.exists(userKey)){
            // 获取过期时间
            expireTime = jedis.ttl(userKey);
            // 给购物车的key 设置
            jedis.expire(cartKey,expireTime.intValue());
        }else {
            // 给购物车的key 设置
            jedis.expire(cartKey,7*24*3600);
        }
    }
}
