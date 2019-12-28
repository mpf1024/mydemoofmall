package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.SpuImage;
import com.atguigu.gmall.bean.SpuInfo;

import java.util.List;

public interface SpuInfoService {

    List<SpuInfo> getSpuInfoList(SpuInfo spuInfo);

    void saveSpuInfo(SpuInfo spuInfo);

    List<SpuImage> getSpuImageList(String spuId);
}
