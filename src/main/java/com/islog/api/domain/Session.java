package com.islog.api.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import java.util.UUID;

import com.islog.api.domain.Member;
import org.springframework.data.util.Lazy;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Session {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String accessToken;

    @ManyToOne
    private Member member;

    @Builder
    public Session(Member member) {
        this.accessToken = UUID.randomUUID().toString();
        this.member = member;
    }
}
