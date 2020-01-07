package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.sku.SkuInfo;
import com.atguigu.gmall.bean.spu.SpuImage;
import com.atguigu.gmall.bean.spu.SpuInfo;
import com.atguigu.gmall.bean.spu.SpuSaleAttr;

import java.util.List;

public interface SpuInfoService {

    /**
     *
     * @param spuInfo -
     * @return 所有的spuInfo
     */
    List<SpuInfo> getSpuInfoList(SpuInfo spuInfo);

    /**
     * 保存spu信息，包括其中的图片，销售属性和销售属性值
     * @param spuInfo -
     */
    void saveSpuInfo(SpuInfo spuInfo);

    /**
     *
     * @param spuId -
     * @return 指定spuId的图片信息
     */
    List<SpuImage> getSpuImageList(String spuId);

    /**
     * 获取指定spuId的销售属性及其销售属性值
     */
    List<SpuSaleAttr> getSpuSaleAttrList(String spuId);

    List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(SkuInfo skuInfo);


}
