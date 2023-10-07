package com.islog.api.service;

import com.islog.api.domain.Member;
import com.islog.api.domain.Session;
import com.islog.api.repository.MemberRepository;
import com.islog.api.exception.InvalidSigninInformation;
import com.islog.api.request.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long signin(Login login) {
        Member member = memberRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(InvalidSigninInformation::new);
        Session session = member.addSession();

        return member.getId();
        // JWT 생성시 subject에 사용자 ID를 넣기 위해 로그인한 사용자에 대한 반환값으로 사용자 ID를 반환한다.
    }
}
