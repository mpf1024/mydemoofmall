package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.SkuESInfo;
import com.atguigu.gmall.bean.SkuInfo;
import com.atguigu.gmall.service.ListService;
import com.atguigu.gmall.service.SkuInfoService;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;

@RestController
@CrossOrigin
public class SkuInfoManageController {
    @Reference
    private SkuInfoService skuInfoService;

    @Reference
    private ListService listService;

    @RequestMapping("saveSkuInfo")
    public String saveSkuInfo(@RequestBody SkuInfo skuInfo){
        skuInfoService.saveSkuInfo(skuInfo);
        return "OK";
    }

    @RequestMapping("onSale")
    public void onSale(String skuId){
        SkuInfo skuInfo = skuInfoService.getSkuInfoById(skuId);
        SkuESInfo skuESInfo = new SkuESInfo();
        // 属性拷贝
        try {
            BeanUtils.copyProperties(skuESInfo,skuInfo);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        listService.saveSkuInfo(skuESInfo);
    }
}
