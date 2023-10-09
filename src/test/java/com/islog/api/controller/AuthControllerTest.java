package com.islog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.islog.api.domain.Member;
import com.islog.api.domain.Session;
import com.islog.api.repository.MemberRepository;
import com.islog.api.repository.SessionRepository;
import com.islog.api.request.Login;
import com.islog.api.request.Signup;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지 접속한다. /test2")
    void test4() throws Exception {
        //given
        Member member = Member.builder()
                .name("백인수")
                .email("saymay10@naver.com")
                .password("1234")
                .build();

        Session session = member.addSession();
        //addSeeion()을 하게 되면 Session 객체가 생성되고, Session 객체가 생성되는 것만으로도
        //accessToken 필드에 값이 들어간다.

        memberRepository.save(member);

        //expected
        mockMvc.perform(get("/test2")
                        .header("Authorization", session.getAccessToken())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 후 검증되지 않은 세션값으로 권한이 필요한 페이지에 접속할 수 없다.")
    void test5() throws Exception {
        //given
        Member member = Member.builder()
                .name("백인수")
                .email("saymay10@naver.com")
                .password("1234")
                .build();

        Session session = member.addSession();
        //addSeeion()을 하게 되면 Session 객체가 생성되고, Session 객체가 생성되는 것만으로도
        //accessToken 필드에 값이 들어간다.

        memberRepository.save(member);

        //expected
        mockMvc.perform(get("/test2")
                        .header("Authorization", session.getAccessToken() + "-ooooo")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입")
    void test6() throws Exception {
        //given
        Signup signup = Signup.builder()
                .email("saymay10@naver.com")
                .name("백인수")
                .password("1234")
                .build();

        //expected
        mockMvc.perform(post("/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signup)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
