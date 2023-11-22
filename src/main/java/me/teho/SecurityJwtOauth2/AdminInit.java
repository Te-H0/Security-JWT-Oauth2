package me.teho.SecurityJwtOauth2;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.teho.SecurityJwtOauth2.user.Member;
import me.teho.SecurityJwtOauth2.user.MemberRepository;
import me.teho.SecurityJwtOauth2.user.MemberRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInit {


    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;

    @PostConstruct
    public void adminInit() {
        Set<MemberRole> roles = new HashSet<>();
        roles.add(MemberRole.ROLE_MEMBER);
        roles.add(MemberRole.ROLE_ADMIN);
        Member member = Member.builder()
                .name("admin")
                .email("admin")
                .roleSet(roles)
                .password(passwordEncoder.encode("admin"))
                .build();
        Member saveMember = memberRepository.save(member);

        log.info("admin 계정 등록 완료.");

    }


}
