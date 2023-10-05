package com.islog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.islog.api.domain.Member;
import com.islog.api.repository.MemberRepository;
import com.islog.api.repository.SessionRepository;
import com.islog.api.request.Login;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공")
    void test() throws Exception {

        //given
        memberRepository.save(Member.builder()
                .name("백인수")
                .email("saymay10@naver.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("saymay10@naver.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        //expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("로그인 성공 후 세션 1개 생성")
    void test2() throws Exception {

        //given
        Member member = memberRepository.save(Member.builder()
                .name("백인수")
                .email("saymay10@naver.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("saymay10@naver.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        //expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        Assertions.assertEquals(1L, member.getSessions().size());
    }

    @Test
    @Transactional
    @DisplayName("로그인 성공 후 세션 응답")
    void test3() throws Exception {

        //given
        Member member = memberRepository.save(Member.builder()
                .name("백인수")
                .email("saymay10@naver.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("saymay10@naver.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        //expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andDo(print());

        Assertions.assertEquals(1L, member.getSessions().size());
    }
}
