package com.atguigu.gmall.bean.sku;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SkuESResult implements Serializable {
    private static final long serialVersionUID = 192321190910L;
    List<SkuESInfo> skuESInfoList;

    long total;

    long totalPages;

    List<String> attrValueIdList;
}
