package com.atguigu.gmall.bean.base;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
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

    @Transient
    private String urlParam;
}
