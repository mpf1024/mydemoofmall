package com.atguigu.gmall.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@ComponentScan("com.atguigu.gmall")
@MapperScan("com.atguigu.gmall.cart.mapper")
public class GmallCartWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(GmallCartWebApplication.class,args);
    }
}
