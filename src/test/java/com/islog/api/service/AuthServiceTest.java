package com.islog.api.service;

import com.islog.api.crypto.ScryptPasswordEncoder;
import com.islog.api.domain.Member;
import com.islog.api.exception.AlreadyExistsEmailException;
import com.islog.api.exception.InvalidSigninInformation;
import com.islog.api.repository.MemberRepository;
import com.islog.api.request.Login;
import com.islog.api.request.Signup;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class AuthServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthService authService;

    @AfterEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void test1(){

//        PasswordEncoder encoder = new PasswordEncoder(); //
        //given
        Signup signup = Signup.builder()
                .email("saymay10@naver.com")
                .name("백인수")
                .password("1234")
                .build();

        //when
        authService.signup(signup);

        //then
        assertEquals(1, memberRepository.count()); //assertEquals의 첫번째 파라미터 갯수, 뒤에 확인 값

        Member member = memberRepository.findAll().iterator().next();
        assertEquals("saymay10@naver.com", member.getEmail());
        assertNotNull(member.getPassword());
        assertEquals("1234",member.getPassword());
        assertEquals("백인수", member.getName());
    }

    @Test
    @DisplayName("회원가입시 중복 이메일")
    void test2(){

        Member member = Member.builder()
                .email("saymay10@naver.com")
                .name("백인수")
                .password("1234")
                .build();

        memberRepository.save(member);

        //given
        Signup signup = Signup.builder()
                .email("saymay10@naver.com")
                .name("백인수")
                .password("1234")
                .build();

        //expected
        assertThrows(AlreadyExistsEmailException.class, () -> authService.signup(signup));

//        assertThrows(AlreadyExistsEmailException.class, new Executable() {
//            @Override
//            public void execute() throws Throwable {
//                authService.signup(signup);
//            }
//        });
    }

    @Test
    @DisplayName("로그인 성공")
    void test3(){

        //given
//        ScryptPasswordEncoder encoder = new ScryptPasswordEncoder();
//        String encrpytedPassword = encoder.encrypt("1234");

        Member member = Member.builder()
                .email("saymay10@naver.com")
                .name("백인수")
                .password("1234")
                .build();
        memberRepository.save(member);

        Login login = Login.builder()
                .email("saymay10@naver.com")
                .password("1234")
                .build();

        //when
        Long memberId = authService.signin(login);

        //then
        Assertions.assertNotNull(memberId);
    }

    @Test
    @DisplayName("로그인시 비밀번호 틀림")
    void test4(){

//        //given
//        Signup signup = Signup.builder()
//                .email("saymay10@naver.com")
//                .name("백인수")
//                .password("1234")
//                .build();
//        authService.signup(signup);
        //given
        ScryptPasswordEncoder encoder = new ScryptPasswordEncoder();
        String encrpytedPassword = encoder.encrypt("1234");

        Member member = Member.builder()
                .email("saymay10@naver.com")
                .name("백인수")
                .password(encrpytedPassword)
                .build();
        memberRepository.save(member);

        Login login = Login.builder()
                .email("saymay10@naver.com")
                .password("5678")
                .build();

        //expected
        Assertions.assertThrows(InvalidSigninInformation.class, () -> authService.signin(login));
    }
}