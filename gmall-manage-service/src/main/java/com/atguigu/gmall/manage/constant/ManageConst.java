package com.atguigu.gmall.manage.constant;

public class ManageConst {
    //redis缓存的key的键前缀
    public static final String SKUKEY_PREFIX="sku:";
    //后缀
    public static final String SKUKEY_SUFFIX=":info";
    //timeout
    public static final int SKUKEY_TIMEOUT=7*24*60*60;

    //锁失效时间
    public static final int SKULOCK_EXPIRE_PX=10000;

    public static final String SKULOCK_SUFFIX=":lock";
}
