package com.atguigu.gmall.bean.spu;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
public class SpuSaleAttrValue implements Serializable {
    private static final long serialVersionUID = 12421940041L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column
    private String spuId;

    @Column
    private String saleAttrId;

    @Column
    private String saleAttrValueName;

    @Transient
    private String isChecked;

}
