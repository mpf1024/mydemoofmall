package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.*;

import java.util.List;

public interface BaseManageService {

    List<BaseCatalog1> getCatalog1();

    List<BaseCatalog2> getCatalog2(String catalog1Id);

    List<BaseCatalog3> getCatalog3(String catalog2Id);

    List<BaseAttrInfo> getAttrList(String catalog3Id);

    /**
     * 根据ID是否存在来保存或更新属性及其属性值
     * @param baseAttrInfo 属性信息
     */
    void saveAttrInfo(BaseAttrInfo baseAttrInfo);

    List<BaseAttrValue> getAttrValueByAttrId(String attrId);

    BaseAttrInfo getAttrInfoById(String attrId);

    List<BaseSaleAttr> getBaseSaleAttrList();
}
