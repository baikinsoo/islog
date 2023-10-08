package com.islog;

import com.islog.api.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class) //스프링이 뜰 때 해당 클래스 등록
//1. Appconfig에 @Configuration 다는 대신 해당 클래스를 여기에 입력하는 방법이 있다.
@SpringBootApplication
public class IslogApplication {

    public static void main(String[] args) {
        SpringApplication.run(IslogApplication.class, args);
    }
}
