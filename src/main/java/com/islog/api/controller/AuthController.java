package com.islog.api.controller;

import com.islog.api.domain.Member;
import com.islog.api.exception.InvalidRequest;
import com.islog.api.exception.InvalidSigninInformation;
import com.islog.api.repository.MemberRepository;
import com.islog.api.request.Login;
import com.islog.api.response.SessionResponse;
import com.islog.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    //    private final MemberRepository memberRepository;
// Service를 이용해서 DB에 접근 할 것이다.
    private final AuthService authService;

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login){
        //json 아이디/비밀번호
        log.info(">>>login={}", login);

        //DB에서 조회
//        Member member = memberRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
//                .orElseThrow(InvalidSigninInformation::new);
        String accessToken = authService.signin(login);

        return new SessionResponse(accessToken);

        //토큰을 응답
//        return member;
        // 반환값을 void로 변경한다.
    }
}
