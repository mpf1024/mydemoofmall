package com.atguigu.gmall.bean.sku;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class SkuSaleAttrValue implements Serializable {
    private static final long serialVersionUID = 192021129090L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String id;

    @Column
    String skuId;

    @Column
    String saleAttrId;

    @Column
    String saleAttrValueId;

    @Column
    String saleAttrName;

    @Column
    String saleAttrValueName;
}
