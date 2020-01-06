package com.atguigu.gmall.list;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.atguigu.gmall")
public class GmallListApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallListApplication.class,args);
    }
}
