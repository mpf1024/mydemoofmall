package com.atguigu.gmall.bean.base;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class BaseCatalog3 implements Serializable {

    private static final long serialVersionUID = 192022903L;
    @Id
    @Column
    private String id;
    @Column
    private String name;
    @Column
    private String catalog2Id;
}
