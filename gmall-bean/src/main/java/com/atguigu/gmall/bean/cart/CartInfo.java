package com.atguigu.gmall.bean.cart;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CartInfo implements Serializable {
    private static final long serialVersionUID = 1192022111290L;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column
    String id;
    @Column
    String userId;
    @Column
    String skuId;
    @Column
    BigDecimal cartPrice;
    @Column
    Integer skuNum;
    @Column
    String imgUrl;
    @Column
    String skuName;
    @Column
    String isChecked="1";

    // 实时价格
    @Transient
    BigDecimal skuPrice;
}
