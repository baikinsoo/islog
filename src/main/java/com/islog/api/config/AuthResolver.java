package com.islog.api.config;

import com.islog.api.config.data.UserSession;
import com.islog.api.domain.Session;
import com.islog.api.exception.Unauthorized;
import com.islog.api.repository.SessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;

    private static final String KEY = "2M3upKjAHLWa9Rx1vo/ozgimEYTZqk1OjK7fWZzORoM=";


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
////        String accessToken = webRequest.getHeader("Authorization");
//        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
//        if(servletRequest == null){
//            log.error("servletRequest null");
//            throw new Unauthorized();
//        }
//        Cookie[] cookies = servletRequest.getCookies();
//        if (cookies.length == 0){
//            log.error("쿠키가 없음");
//            throw new Unauthorized();
//        }
//
//        String accessToken = cookies[0].getValue();

//        if (accessToken == null || accessToken.equals("")) {
//            throw new Unauthorized();
//        }

//        //데이터베이스 사용자 확인 작업
//        Session session = sessionRepository.findByAccessToken(accessToken)
//                .orElseThrow(Unauthorized::new);

//        return new UserSession(session.getMember().getId());


        //-----------------------JWT
        String jws = webRequest.getHeader("Authorization");
        if(jws == null || jws.equals("")){
            throw new Unauthorized();
        }

        byte[] decodedKey = Base64.decodeBase64(KEY);
        //복호화 한 것이다.
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(decodedKey)
                    .build()
                    .parseSignedClaims(jws);
            String memberId = claims.getBody().getSubject();
            return new UserSession(Long.parseLong(memberId));
        } catch (JwtException e) {
            throw new Unauthorized();
        }
    }
}
