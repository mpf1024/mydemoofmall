package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.*;

import java.util.List;

public interface BaseManageService {

    List<BaseCatalog1> getCatalog1();

    List<BaseCatalog2> getCatalog2(String catalog1Id);

    List<BaseCatalog3> getCatalog3(String catalog2Id);

    /**
     *
     * @param catalog3Id 三级分类ID
     * @return 三级分类的平台属性
     */
    List<BaseAttrInfo> getAttrList(String catalog3Id);

    /**
     * 根据属性值ID获取平台属性的名字和值
     * @param valueIds 平台属性值的ID
     */
    List<BaseAttrInfo> getAttrList(List<String> valueIds);

    /**
     * 根据ID是否存在来保存或更新基本平台属性和属性值
     * @param baseAttrInfo 属性信息
     */
    void saveAttrInfo(BaseAttrInfo baseAttrInfo);

    /**
     *
     * @param attrId 平台属性ID
     * @return 指定的平台属性值
     */
    List<BaseAttrValue> getAttrValueByAttrId(String attrId);

    /**
     *
     * @param attrId 平台属性ID
     * @return 平台属性信息，包括属性值 如果不在指定属性，返回null
     */
    BaseAttrInfo getAttrInfoById(String attrId);

    /**
     *
     * @return 所有基本销售属性
     */
    List<BaseSaleAttr> getBaseSaleAttrList();


}
