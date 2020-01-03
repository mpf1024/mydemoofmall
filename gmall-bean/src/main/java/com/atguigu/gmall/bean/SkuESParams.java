package com.atguigu.gmall.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class SkuESParams implements Serializable {
    private static final long serialVersionUID = 10221190910L;
    String  keyword;

    String catalog3Id;

    String[] valueId;

    int pageNo=1;

    int pageSize=20;
}
