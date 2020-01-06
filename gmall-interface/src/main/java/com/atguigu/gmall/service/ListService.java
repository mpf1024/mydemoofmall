package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.SkuESInfo;
import com.atguigu.gmall.bean.SkuESParams;
import com.atguigu.gmall.bean.SkuESResult;

public interface ListService {

    /**
     * 保存到elasticsearch
     * @param skuESInfo -
     */
    void saveSkuInfo(SkuESInfo skuESInfo);

    /**
     * 从elasticsearch查询
     * @param skuESParams -
     */
    SkuESResult search(SkuESParams skuESParams);

    /**
     * 更新redis热点点击计数
     * @param skuId 商品ID
     */
    void incrHotScore(String skuId);
}
