package com.atguigu.gmall.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.cart.CartInfo;
import com.atguigu.gmall.bean.sku.SkuInfo;
import com.atguigu.gmall.cart.constant.CartConst;
import com.atguigu.gmall.cart.mapper.CartInfoMapper;
import com.atguigu.gmall.config.RedisUtil;
import com.atguigu.gmall.service.CartInfoService;
import com.atguigu.gmall.service.SkuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class CartInfoServiceImpl implements CartInfoService {

    @Autowired
    private CartInfoMapper cartInfoMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Reference
    private SkuInfoService skuInfoService;

    @Override
    public CartInfo addToCart(String skuId, String userId, Integer skuNum) {
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

        //判断缓存中是否有，防止缓存意外丢失了
        if (!jedis.exists(cartKey)) {
            loadCartCache(userId);//缓存中没有先载入缓存
        }
        Example example = new Example(CartInfo.class);
        example.createCriteria().andEqualTo("skuId", skuId)
                .andEqualTo("userId", userId);
        //去数据库查询指定商品
        CartInfo cartInfoExist = cartInfoMapper.selectOneByExample(example);

        if (cartInfoExist != null) {
            //数据库购物车已经添加过此商品
            cartInfoExist.setSkuNum(cartInfoExist.getSkuNum() + skuNum);
            //实时价格
            cartInfoExist.setSkuPrice(cartInfoExist.getCartPrice());
            //更新数量
            CartInfo cartInfo = new CartInfo();
            cartInfo.setId(cartInfoExist.getId());
            cartInfo.setSkuNum(cartInfoExist.getSkuNum());
            cartInfoMapper.updateByPrimaryKeySelective(cartInfo);
        } else {
            //不存在
            SkuInfo skuInfo = skuInfoService.getSkuInfoById(skuId);
            CartInfo cartInfo = new CartInfo();
            cartInfo.setSkuId(skuId);
            cartInfo.setUserId(userId);
            cartInfo.setCartPrice(skuInfo.getPrice());
            cartInfo.setSkuPrice(skuInfo.getPrice());
            cartInfo.setSkuNum(skuNum);
            cartInfo.setSkuName(skuInfo.getSkuName());
            cartInfo.setImgUrl(skuInfo.getSkuDefaultImg());
            //保存到数据库
            cartInfoMapper.insertSelective(cartInfo);
            cartInfoExist = cartInfo;
        }

        // 更新redis 放在最后！
        String cartInfoJson = JSON.toJSONString(cartInfoExist);
        jedis.hset(cartKey, skuId, cartInfoJson);
        setCartkeyExpireTime(userId, jedis, cartKey);

        // 关闭redis！
        jedis.close();
        return cartInfoExist;
    }

    @Override
    public List<CartInfo> getCartList(String userId) {
        //先从redis缓存中取
        String cartKey = makeCartKey(userId);
        Jedis jedis = redisUtil.getJedis();
        List<String> cartInfoStr = jedis.hvals(cartKey);
        List<CartInfo> cartInfoList = new ArrayList<>();
        if (cartInfoStr != null && cartInfoStr.size() > 0) {
            for (String s : cartInfoStr) {
                CartInfo cartInfo = JSON.parseObject(s, CartInfo.class);
                cartInfoList.add(cartInfo);
            }
            cartInfoList.sort(Comparator.comparing(CartInfo::getId));
            return cartInfoList;
        }

        //缓存中没有，从数据库取并放入缓存
        cartInfoList = loadCartCache(userId);

        return cartInfoList;
    }

    @Override
    public List<CartInfo> mergeToCartList(String tempUserId, String userId) {
        //查询登录时的购物车
        List<CartInfo> cartInfoListLogin = getCartList(userId);
        List<CartInfo> cartTempList = getCartList(tempUserId);
        if(cartTempList == null || cartTempList.size() == 0){
            return cartInfoListLogin;//未登录购物车为空，直接返回已登录购物车
        }

        if(cartInfoListLogin != null && cartInfoListLogin.size() > 0){
            for (CartInfo cartInfoTemp : cartTempList) {
                boolean isMatched = false;
                for (CartInfo cartInfoLogin : cartInfoListLogin) {
                    if(cartInfoTemp.getSkuId().equals(cartInfoLogin.getSkuId())){
                        //表示存在未登录购物车商品与登录的购物车商品一样
                        //以未登录的为基准，保存未登录的商品数量和选中状态
                        cartInfoLogin.setIsChecked(cartInfoTemp.getIsChecked());
                        cartInfoLogin.setSkuNum(cartInfoTemp.getSkuNum());
                        cartInfoMapper.updateByPrimaryKeySelective(cartInfoLogin);
                        isMatched = true;
                        break;
                    }
                }
                //已登录购物车中没有该商品  直接添加到数据库
                if(!isMatched){
                    cartInfoTemp.setId(null);
                    cartInfoTemp.setUserId(userId);
                    cartInfoMapper.insertSelective(cartInfoTemp);
                }
            }
        }

        deleteCartList(tempUserId);//删除为登录购物车数据

        return loadCartCache(userId);
    }
    @Override
    public void deleteCartList(String tempUserId) {
        Example example = new Example(CartInfo.class);
        example.createCriteria().andEqualTo("userId", tempUserId);
        cartInfoMapper.deleteByExample(example);

        // 删除缓存
        Jedis jedis = redisUtil.getJedis();
        String cartKey = makeCartKey(tempUserId);
        jedis.del(cartKey);

        jedis.close();
    }

    @Override
    public void checkCart(String isChecked, String skuId, String userId) {
        Jedis jedis = redisUtil.getJedis();
        String cartKey = makeCartKey(userId);
        String cartInfoJson = jedis.hget(cartKey, skuId);

        jedis.hdel(cartKey, skuId);//先删除原来的

        CartInfo cartInfo = JSON.parseObject(cartInfoJson, CartInfo.class);
        cartInfo.setIsChecked(isChecked);//更改状态
        jedis.hset(cartKey, skuId, JSON.toJSONString(cartInfo));//再保存

    }

    @Override
    public List<CartInfo> getCartCheckedList(String userId) {
        String cartKey = makeCartKey(userId);
        Jedis jedis = redisUtil.getJedis();
        List<String> cartInfoStrList = jedis.hvals(cartKey);
        List<CartInfo> cartInfoList = new ArrayList<>();
        if (cartInfoStrList != null && cartInfoStrList.size() > 0) {
            for (String cartInfoStr : cartInfoStrList) {
                CartInfo cartInfo = JSON.parseObject(cartInfoStr,CartInfo.class);
                if("1".equals(cartInfo.getIsChecked())){
                    cartInfoList.add(cartInfo);
                }
            }
        }
        jedis.close();
        return cartInfoList;
    }

    //从数据库中查询购物车并放入缓存中
    private List<CartInfo> loadCartCache(String userId) {
        //缓存中没有，去数据库中取,并且查询skuinfo的实时价格
        List<CartInfo> cartInfoList = cartInfoMapper.selectCartListWithCurPrice(userId);
        if (cartInfoList != null && cartInfoList.size() > 0) {
            String cartKey = makeCartKey(userId);
            Jedis jedis = redisUtil.getJedis();
            Map<String, String> map = new HashMap<>();
            for (CartInfo cartInfo : cartInfoList) {
                String cartInfoJson = JSON.toJSONString(cartInfo);
                map.put(cartInfo.getSkuId(), cartInfoJson);
            }
            //保存到缓存中
            jedis.hmset(cartKey, map);
            //设置过期时间
            setCartkeyExpireTime(userId, jedis, cartKey);
            return cartInfoList;
        }
        return null;
    }

    private void setCartkeyExpireTime(String userId, Jedis jedis, String cartKey) {
        // 根据user的过期时间设置
        // 获取用户的过期时间 user:userId:info
        String userKey = CartConst.USER_KEY_PREFIX + userId + CartConst.USERINFOKEY_SUFFIX;
        // 用户key 存在，登录。
        Long expireTime;
        if (jedis.exists(userKey)) {
            // 获取过期时间
            expireTime = jedis.ttl(userKey);
            // 给购物车的key 设置
            jedis.expire(cartKey, expireTime.intValue());
        } else {
            // 给购物车的key 设置
            jedis.expire(cartKey, 7 * 24 * 3600);
        }
    }

    private String makeCartKey(String userId){
        return CartConst.USER_KEY_PREFIX + userId + CartConst.USER_CART_KEY_SUFFIX;
    }
}
