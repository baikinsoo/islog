package com.islog.api.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    private LocalDateTime createdAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "member")
    private List<Session> sessions = new ArrayList<>();

    @Builder
    public Member(Long id, String name, String email, String password, LocalDateTime createdAt, List<Session> sessions) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }

    public Session addSession() {
        Session session = Session.builder()
                .member(this)
                .build();

        sessions.add(session);

        return session;
    }
}
