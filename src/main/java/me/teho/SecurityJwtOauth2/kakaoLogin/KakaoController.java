package me.teho.SecurityJwtOauth2.kakaoLogin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.teho.SecurityJwtOauth2.user.MemberService;
import me.teho.SecurityJwtOauth2.user.MemberSignInDto;
import me.teho.SecurityJwtOauth2.user.MemberSignUpDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;
    private final MemberService memberService;

    @GetMapping("/kakao/callback")
    public String kakaoCallback(@RequestParam String code) {
        /**
         * post 방식으로 key=value 데이터를 카카오로 요청
         * Retrofit2, OkHttp, RestTemplate 사용 가능
         */
        log.info("kakao controller 들어오냐?");
        KakaoProfile kakaoProfile = kakaoService.kakaoRequest(code);
        log.info("kakaoProfile의 사용자 닉네임 => {} ", kakaoProfile.getProperties().getNickname());

        String kakaoUserEmail = kakaoProfile.getKakao_account().getEmail();
        String kakaoUserName = kakaoProfile.getProperties().getNickname();
        Long kakaoUserProviderId = kakaoProfile.getId();
        String kakaoUserPw = kakaoUserEmail + '_' + kakaoUserProviderId;
        if (kakaoService.isNew(kakaoUserEmail)) {
            log.info("{}는 처음 들어온 회원이여서 자동 회원 가입 진행", kakaoUserName);
            MemberSignUpDto kakaoMemberSignUp = MemberSignUpDto.builder()
                    .name(kakaoUserName)
                    .email(kakaoUserEmail)
                    .password(kakaoUserPw)
                    .build();
            memberService.join(kakaoMemberSignUp);
        }

        log.info("{} 카카오로 로그인 프로세스 진행", kakaoUserName);
        MemberSignInDto kakaoMemberSignIn = MemberSignInDto.builder()
                .password(kakaoUserPw)
                .email(kakaoUserEmail)
                .build();
        String jwt = memberService.login(kakaoMemberSignIn);
        return jwt;
    }

}
