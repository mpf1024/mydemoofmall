package com.atguigu.gmall.manage.mapper;

import com.atguigu.gmall.bean.BaseAttrInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BaseAttrInfoMapper extends Mapper<BaseAttrInfo> {

    /**
     *  查询指定三级分类的平台属性及其属性值
     * @param catalog3Id 三级分类ID
     * @return List&lt;BaseAttrInfo&gt; 其中的BaseAttrValue集合也装配好
     */
    List<BaseAttrInfo> getBaseAttrInfoListByCatalog3Id(String catalog3Id);
}
