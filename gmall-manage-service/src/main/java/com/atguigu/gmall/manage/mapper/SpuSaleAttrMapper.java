package com.atguigu.gmall.manage.mapper;

import com.atguigu.gmall.bean.spu.SpuSaleAttr;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpuSaleAttrMapper extends Mapper<SpuSaleAttr> {

    /**
     *  只查询出所有销售属性和销售属性值
     */
    List<SpuSaleAttr> selectSpuSaleAttrList(String spuId);

    /**
     * 列出所有该spu的销售属性和属性值，
     * 并关联某skuid如果能关联上is_check设为1，否则设为0。
     */
    List<SpuSaleAttr> selectSpuSaleAttrListCheckBySku(@Param("skuId") String skuId, @Param("spuId") String spuId);

}
