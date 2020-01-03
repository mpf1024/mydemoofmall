package com.atguigu.gmall.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SkuESResult implements Serializable {
    private static final long serialVersionUID = 192321190910L;
    List<SkuESInfo> skuLsInfoList;

    long total;

    long totalPages;

    List<String> attrValueIdList;
}
