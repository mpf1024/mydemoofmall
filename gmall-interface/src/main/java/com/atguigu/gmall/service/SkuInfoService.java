package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.sku.SkuInfo;
import com.atguigu.gmall.bean.sku.SkuSaleAttrValue;

import java.util.List;

public interface SkuInfoService {

    /**
     * 保存信息，包括其中的图片，属性即属性值
     * @param skuInfo -
     */
    void saveSkuInfo(SkuInfo skuInfo);

    /**
     *  根据ID获取sku信息，包括图片信息
     * @param skuId -
     */
    SkuInfo getSkuInfoById(String skuId);

    List<SkuSaleAttrValue> getSkuSaleAttrValueListBySpu(String spuId);
}
