package com.islog.api.controller;

import com.islog.api.config.AppConfig;
import com.islog.api.domain.Member;
import com.islog.api.exception.InvalidRequest;
import com.islog.api.exception.InvalidSigninInformation;
import com.islog.api.repository.MemberRepository;
import com.islog.api.request.Login;
import com.islog.api.request.Signup;
import com.islog.api.response.SessionResponse;
import com.islog.api.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    //    private final MemberRepository memberRepository;
// Service를 이용해서 DB에 접근 할 것이다.
    private final AuthService authService;

    private final AppConfig appConfig;
//    private static final String KEY = "2M3upKjAHLWa9Rx1vo/ozgimEYTZqk1OjK7fWZzORoM=";
//    public static final String KEY2 = "thisIsSecretKeythisIsSecretKeythisIsSecretKeythisIsSecretKeythisIsSecretKey";

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login){
        //json 아이디/비밀번호
        log.info(">>>login={}", login);

        //DB에서 조회
//        Member member = memberRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
//                .orElseThrow(InvalidSigninInformation::new);
        Long memberId = authService.signin(login);

        //------------JWT

////        SecretKey key = Jwts.SIG.HS256.key().build();

        //--------key 생성
//        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//        byte[] encodedKey = key.getEncoded();
//        String strKey = Base64.getEncoder().encodeToString(encodedKey);
////        2M3upKjAHLWa9Rx1vo/ozgimEYTZqk1OjK7fWZzORoM=

        //-----key 생성으로 생성한 키 decode한 뒤 byte code로 생성
//        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(KEY));

        String jws = Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .signWith(SignatureAlgorithm.HS256, appConfig.jwtKey)
                .setIssuedAt(new Date())
                // 계속 토큰을 바뀌게 한다.
//                .signWith(SignatureAlgorithm.HS256, KEY2)
                .compact();

        //암호화

        log.info(">>>>>>>>>> {}", jws);

        return new SessionResponse(jws);

        //------------------------------------
//        // 쿠키 만들기
//        ResponseCookie cookie = ResponseCookie.from("SESSION", accessToken)
//                .domain("localhost") // todo 서버 환경에 따른 분리 필요
//                .path("/")
//                .httpOnly(true)
//                .secure(false)
//                .maxAge(Duration.ofDays(30))
//                .sameSite("Strict")
//                .build();//from을 통해 쿠키의 key, value를 넣을 수 있다.
//
//        log.info(">>>>>>>>>>>>> cookie={}", cookie.toString());
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, cookie.toString())
//                .build();


//        return new SessionResponse(accessToken);

        //토큰을 응답
//        return member;
        // 반환값을 void로 변경한다.
    }

    @PostMapping("/auth/signup")
    public void signup(@RequestBody Signup signup) {
        authService.signup(signup);
    }
}
