package com.islog.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

//@Configuration //2번째 방법 @Configuration을 달면된다. -> Application 파일에 @Enable~~~ 등록하는 대신
@ConfigurationProperties(prefix = "bis") //스프링이 처음 뜰 때 이게 bis가 등록되어야 한다.
@Data
public class AppConfig {

//    public static final String KEY2 = "thisIsSecretKeythisIsSecretKeythisIsSecretKeythisIsSecretKeythisIsSecretKey";

//    public String hello;
//    public List<String> hello;
//    public Map<String,String> hello;

//    public Hello hello;
//
//    @Data
//    public static class Hello{
//        public String name;
//        public String home;
//        public Long age;
//    }

    public String jwtKey;
}

