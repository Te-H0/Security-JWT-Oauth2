package me.teho.SecurityJwtOauth2.user;

import lombok.RequiredArgsConstructor;
import me.teho.SecurityJwtOauth2.jwt.JwtProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public MemberSignUpDto join(MemberSignUpDto memberSignUpDto) {

        Member member = Member.builder()
                .name(memberSignUpDto.getName())
                .email(memberSignUpDto.getEmail())
                .password(passwordEncoder.encode(memberSignUpDto.getPassword()))
                .build();
        return MemberSignUpDto.from(memberRepository.save(member));
    }

    public String login(MemberSignInDto memberSignInDto) {
        Member loginMember = memberRepository.findMemberByEmail(memberSignInDto.getEmail()).orElseThrow(() -> new RuntimeException("No exist User"));
        if (!passwordEncoder.matches(memberSignInDto.getPassword(), loginMember.getPassword())) {
            throw new RuntimeException("PW ERROR!");
        }
        String jwt = jwtProvider.generateAccessToken(loginMember.getEmail());
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
