package com.gk.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger配置类
 *
 * @author Kevin
 * @date 2020-03-12 16:11
 **/

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger.show}")
    private boolean swaggerShow;

    @Value("${swagger.basePackage}")
    private String basePackagePath;

    // 配置api信息
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerShow)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackagePath))
                .paths(PathSelectors.any())
                .build();
    }

    // 应用描述信息
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("工会预约管理系统 RESTful APIs")
                .description("工会预约管理系统 - 接口列表")
                .termsOfServiceUrl("")
                .contact(new Contact("工会预约管理系统", "http://admin.gk.com", "contact@163.com"))
                .version("1.0").build();
    }
}
