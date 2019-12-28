package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.SpuImage;
import com.atguigu.gmall.bean.SpuInfo;

import java.util.List;

public interface SpuInfoService {

    /**
     *
     * @param spuInfo -
     * @return 所有的spuInfo
     */
    List<SpuInfo> getSpuInfoList(SpuInfo spuInfo);

    /**
     * 保存spu信息，包括其中的图片，销售属性
     * @param spuInfo -
     */
    void saveSpuInfo(SpuInfo spuInfo);

    /**
     *
     * @param spuId -
     * @return 指定spuId的图片信息
     */
    List<SpuImage> getSpuImageList(String spuId);
}
