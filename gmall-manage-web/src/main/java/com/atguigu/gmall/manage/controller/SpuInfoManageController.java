package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.spu.SpuImage;
import com.atguigu.gmall.bean.spu.SpuInfo;
import com.atguigu.gmall.bean.spu.SpuSaleAttr;
import com.atguigu.gmall.service.SpuInfoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class SpuInfoManageController {

    @Reference
    private SpuInfoService spuInfoService;

    /**
     * 仅获取spu信息，不包括图片和销售属性
     */
    @RequestMapping("spuList")
    public List<SpuInfo> spuList(String catalog3Id){
        SpuInfo spuInfo = new SpuInfo();
        spuInfo.setCatalog3Id(catalog3Id);
        return spuInfoService.getSpuInfoList(spuInfo);
    }

    /**
     * 保存spu信息，包括其中的图片，销售属性和销售属性值\
     */
    @RequestMapping("saveSpuInfo")
    public String saveSpuInfo(@RequestBody SpuInfo spuInfo){
        spuInfoService.saveSpuInfo(spuInfo);
        return  "OK";
    }

    //图片信息
    @RequestMapping("spuImageList")
    public List<SpuImage> spuImageList(String spuId){
        return spuInfoService.getSpuImageList(spuId);
    }

    //获取指定spuId的销售属性及其销售属性值
    @RequestMapping("spuSaleAttrList")
    public List<SpuSaleAttr> getSpuSaleAttrList(String spuId){
        return spuInfoService.getSpuSaleAttrList(spuId);
    }
}
