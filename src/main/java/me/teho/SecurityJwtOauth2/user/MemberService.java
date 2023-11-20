package me.teho.SecurityJwtOauth2.user;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.teho.SecurityJwtOauth2.jwt.JwtProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public MemberSignUpDto join(MemberSignUpDto memberSignUpDto) {

        if (memberRepository.findMemberByEmail(memberSignUpDto.getEmail()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }
        // 가입되어 있지 않은 회원이면,
        // 권한 정보 만들고
        Authority authority = authorityRepository.findByAuthorityName("ROLE_USER").orElse(null);
        if (authority == null) {
            Authority roleUser = Authority.builder()
                    .authorityName("ROLE_USER")
                    .build();
            authority = authorityRepository.save(roleUser);
        }

        Member member = Member.builder()
                .name(memberSignUpDto.getName())
                .email(memberSignUpDto.getEmail())
                .password(passwordEncoder.encode(memberSignUpDto.getPassword()))
                .authorities(Collections.singleton(authority))
                .build();
        return MemberSignUpDto.from(memberRepository.save(member));
    }

    public String login(MemberSignInDto memberSignInDto) {
//        Member loginMember = memberRepository.findMemberByEmail(memberSignInDto.getEmail()).orElseThrow(() -> new RuntimeException("No exist User"));
//        if (!passwordEncoder.matches(memberSignInDto.getPassword(), loginMember.getPassword())) {
//            throw new RuntimeException("PW ERROR!");
//        }
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(memberSignInDto.getEmail(), memberSignInDto.getPassword());
//
//        // authenticate 메소드가 실행이 될 때 CustomUserDetailsService class의 loadUserByUsername 메소드가 실행
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        // 해당 객체를 SecurityContextHolder에 저장하고
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        // authentication 객체를 createToken 메소드를 통해서 JWT Token을 생성
//        String jwt = jwtProvider.generateAccessToken(authentication);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(memberSignInDto.getEmail(), memberSignInDto.getPassword());

        // authenticate 메소드가 실행이 될 때 CustomUserDetailsService class의 loadUserByUsername 메소드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.info("===========>{}", authentication.getAuthorities());
        // 해당 객체를 SecurityContextHolder에 저장하고
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // authentication 객체를 createToken 메소드를 통해서 JWT Token을 생성
        String jwt = jwtProvider.generateAccessToken(authentication);
        //claim 열어보기 => user의 이메일
        Claims claims = jwtProvider.getClaims(jwt);
        log.info("User의 claim의 subject(email) => {}", claims.getSubject());
        log.info("여기까지와?");
        return jwt;

    }

//    /**
//     * member id로 회원 조회
//     */
//    public Optional<Member> findMemberById(Long id) {
//        return memberRepository.findMemberById(id);
//    }
//
//    /**
//     * token 일치 확인
//     */
//    public boolean verifyWithMemberToken(Member member, String token) {
//        return member.getAccessToken().equals(token);
//    }

//    @Transactional
//    public String genAccessToken(Member member) {
//        String accessToken = member.getAccessToken();
//
//        if (StringUtils.hasLength(accessToken) == false) {
//            accessToken = jwtProvider.generateAccessToken(member.getEmail());
//        }
//
//        return accessToken;
//    }
}
