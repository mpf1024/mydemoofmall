package com.atguigu.gmall.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class BaseSaleAttr implements Serializable {
    private static final long serialVersionUID = 1920229013L;

    @Id
    @Column
    String id ;

    @Column
    String name;

}
