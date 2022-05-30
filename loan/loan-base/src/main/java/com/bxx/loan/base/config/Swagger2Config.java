package com.bxx.loan.base.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * @author : bu
 * @date : 2022/5/17  12:18
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket adminApiDocketConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("adminApi")
                .apiInfo(adminApiInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/admin/.*")))
                .build();
    }

    public ApiInfo adminApiInfo(){
        return new ApiInfoBuilder()
                .title("尚融宝管理员后台管理api文档")
                .description("该系统的所有管理员api接口信息")
                .contact(new Contact("Bxx", "https://github.com/bxianghui", "1241721228@qq.com"))
                .build();
    }

    @Bean
    public Docket webApiDocketConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/web/.*")))
                .build();
    }

    public ApiInfo webApiInfo(){
        return new ApiInfoBuilder()
                .title("尚融宝普通用户后台管理api文档")
                .description("该系统的所有用户api接口信息")
                .contact(new Contact("Bxx", "https://github.com/bxianghui", "1241721228@qq.com"))
                .build();
    }

}
