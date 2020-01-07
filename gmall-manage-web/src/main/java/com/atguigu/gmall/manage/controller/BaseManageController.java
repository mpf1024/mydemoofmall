package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.base.*;
import com.atguigu.gmall.service.BaseManageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class BaseManageController {

    @Reference
    private BaseManageService baseManageService;

    @RequestMapping("getCatalog1")
    public List<BaseCatalog1> getCatalog1(){
        return baseManageService.getCatalog1();
    }

    @RequestMapping("getCatalog2")
    public List<BaseCatalog2> getCatalog2(String catalog1Id){
        return baseManageService.getCatalog2(catalog1Id);
    }

    @RequestMapping("getCatalog3")
    public List<BaseCatalog3> getCatalog3(String catalog2Id){
        return baseManageService.getCatalog3(catalog2Id);
    }

    /**
     *
     * @param catalog3Id 三级分类ID
     * @return 三级分类的平台属性
     */
    @RequestMapping("attrInfoList")
    public List<BaseAttrInfo> attrInfoList(String catalog3Id){
        return baseManageService.getAttrList(catalog3Id);
    }

    /**
     * 更新或添加基本平台属性
     * @param baseAttrInfo -
     */
    @RequestMapping("saveAttrInfo")
    public void saveOrUpdateAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo){
        baseManageService.saveAttrInfo(baseAttrInfo);
    }

    /**
     * 根据平台属性ID获取属性值，如果没有，返回null
     * @param attrId 平台属性ID
     */
    @RequestMapping("getAttrValueList")
    public List<BaseAttrValue> getAttrValue(String attrId){
        BaseAttrInfo attrInfo = baseManageService.getAttrInfoById(attrId);
        return attrInfo == null ? null:attrInfo.getAttrValueList();
    }

    //所有基本销售属性
    @RequestMapping("baseSaleAttrList")
    public List<BaseSaleAttr> getBaseSaleAttr(){
        return baseManageService.getBaseSaleAttrList();
    }
}
