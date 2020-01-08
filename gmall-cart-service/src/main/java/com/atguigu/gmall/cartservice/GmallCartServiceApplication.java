package com.atguigu.gmall.cartservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.atguigu.gmall.cartservice.mapper")
@ComponentScan("com.atguigu.gmall")
public class GmallCartServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(GmallCartServiceApplication.class,args);
    }
}
