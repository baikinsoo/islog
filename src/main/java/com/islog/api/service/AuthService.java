package com.islog.api.service;

import com.islog.api.crypto.PasswordEncoder;
import com.islog.api.crypto.ScryptPasswordEncoder;
import com.islog.api.domain.Member;
import com.islog.api.exception.AlreadyExistsEmailException;
import com.islog.api.repository.MemberRepository;
import com.islog.api.exception.InvalidSigninInformation;
import com.islog.api.request.Login;
import com.islog.api.request.Signup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signin(Login login) {
        //----------- 기존의 기본적인 로그인 방법
//        Member member = memberRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
//                .orElseThrow(InvalidSigninInformation::new);
//        Session session = member.addSession();

        // 암호화를 통한 비밀번호 확인 방법
        Member member = memberRepository.findByEmail(login.getEmail())
                .orElseThrow(InvalidSigninInformation::new);

//        ScryptPasswordEncoder encoder = new ScryptPasswordEncoder();

//        SCryptPasswordEncoder encoder = new SCryptPasswordEncoder(
//                16,
//                8,
//                1,
//                32,
//                64);

        boolean matches = passwordEncoder.matches(login.getPassword(), member.getPassword());
        if(!matches){
            throw new InvalidSigninInformation();
        }

        return member.getId();
        // JWT 생성시 subject에 사용자 ID를 넣기 위해 로그인한 사용자에 대한 반환값으로 사용자 ID를 반환한다.
    }


//    회원 가입
    public void signup(Signup signup) {

        Optional<Member> memberOptional = memberRepository.findByEmail(signup.getEmail());
        if(memberOptional.isPresent()){
            throw new AlreadyExistsEmailException();
        }

//        ScryptPasswordEncoder encoder = new ScryptPasswordEncoder();
        // @Component로 넣으면 된다.

//        SCryptPasswordEncoder encoder = new SCryptPasswordEncoder(
//                16,
//                8,
//                1,
//                32,
//                64);

        String eneryptedPassword = passwordEncoder.encrypt(signup.getPassword());

        Member member = Member.builder()
                .name(signup.getName())
                .password(eneryptedPassword)
                .email(signup.getEmail())
                .build();

        memberRepository.save(member);
    }
}
