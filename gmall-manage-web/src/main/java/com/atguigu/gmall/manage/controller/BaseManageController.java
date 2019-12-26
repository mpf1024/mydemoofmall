package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.service.BaseManageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manage")
public class BaseManageController {

    @Reference
    private BaseManageService baseManageService;

    @GetMapping("getCatalog1")
    public List<BaseCatalog1> getCatalog1(){
        return baseManageService.getCatalog1();
    }

    @GetMapping("getCatalog2")
    public List<BaseCatalog2> getCatalog2(String catalog1Id){
        return baseManageService.getCatalog2(catalog1Id);
    }

    @GetMapping("getCatalog3")
    public List<BaseCatalog3> getCatalog3(String catalog2Id){
        return baseManageService.getCatalog3(catalog2Id);
    }

    @GetMapping("attrInfoList")
    public List<BaseAttrInfo> attrInfoList(String catalog3Id){
        return baseManageService.getAttrList(catalog3Id);
    }

    @RequestMapping("saveAttrInfo")
    public void saveOrUpdateAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo){
        baseManageService.saveAttrInfo(baseAttrInfo);
    }

    @GetMapping("getAttrValueList")
    public List<BaseAttrValue> getAttrValue(String attrId){
        BaseAttrInfo attrInfo = baseManageService.getAttrInfoById(attrId);
        return attrInfo == null ? null:attrInfo.getAttrValueList();
    }
}
