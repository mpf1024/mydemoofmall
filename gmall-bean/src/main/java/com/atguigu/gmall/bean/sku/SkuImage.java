package com.atguigu.gmall.bean.sku;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class SkuImage implements Serializable {
    private static final long serialVersionUID = 192021129090L;
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column
    private String skuId;
    @Column
    private String imgName;
    @Column
    private String imgUrl;
    @Column
    private String spuImgId;
    @Column
    private String isDefault;
}
