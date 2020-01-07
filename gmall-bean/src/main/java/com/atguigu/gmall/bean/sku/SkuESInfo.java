package com.atguigu.gmall.bean.sku;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuESInfo implements Serializable {
    private static final long serialVersionUID = 192022111290910L;
    String id;

    BigDecimal price;

    String skuName;

    String catalog3Id;

    String skuDefaultImg;

    Long hotScore=0L;

    List<SkuESAttrValue> skuAttrValueList;
}
