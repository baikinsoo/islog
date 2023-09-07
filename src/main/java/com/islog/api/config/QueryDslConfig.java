package com.islog.api.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class QueryDslConfig {

    //Querydsl를 사용하기 위한 어노테이션!!!!!!
    @PersistenceContext
    // 엔티티 매니저를 주입받을 때 사용된다.
    public EntityManager em;
    // JPA 구현체가 엔티티 매니저를 자동으로 주입해준다.

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(em);
    }
}
