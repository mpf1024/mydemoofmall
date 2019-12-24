package com.atguigu.gmall.usermanage.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class AppSwagger2Config {
    @Bean
    public Docket projectApis() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("userApi")
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.regex("/*.*"))
                .build().apiInfo(adminApiInfo());
        //.enable(enable);
    }


    private ApiInfo adminApiInfo() {
        return new ApiInfoBuilder()
                .title("userInfo操作API文档")
                .description("本文档描述了对userInfo的相关操作")
                .version("1.0")
                .build();
    }
}
