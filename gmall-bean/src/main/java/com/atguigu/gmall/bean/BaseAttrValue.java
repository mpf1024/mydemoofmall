package com.atguigu.gmall.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class BaseAttrValue implements Serializable {
    private static final long serialVersionUID = 1920129090L;
    @Id
    @Column
    private String id;
    @Column
    private String valueName;
    @Column
    private String attrId;
}
