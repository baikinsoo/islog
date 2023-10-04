package com.islog.api.controller;

import com.islog.api.domain.Member;
import com.islog.api.exception.InvalidRequest;
import com.islog.api.exception.InvalidSigninInformation;
import com.islog.api.repository.MemberRepository;
import com.islog.api.request.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final MemberRepository memberRepository;

    @PostMapping("/auth/login")
    public Member login(@RequestBody Login login){
        //json 아이디/비밀번호
        log.info(">>>login={}", login);
        //DB에서 조회
        Member member = memberRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(InvalidSigninInformation::new);
        //토큰을 응답
        return member;
    }
}
