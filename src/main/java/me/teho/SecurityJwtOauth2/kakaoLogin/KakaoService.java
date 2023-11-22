package me.teho.SecurityJwtOauth2.kakaoLogin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.teho.SecurityJwtOauth2.user.Member;
import me.teho.SecurityJwtOauth2.user.MemberRepository;
import me.teho.SecurityJwtOauth2.user.MemberService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    public KakaoProfile kakaoRequest(String code) {
        RestTemplate rt = new RestTemplate();

        //HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "a7112c7855122c19ace78f063bcce15a");
        params.add("redirect_uri", "http://localhost:8080/kakao/callback");
        params.add("code", code);

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        //Http요청하기 - post방식 - response변수의 응답을 받음
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );
        //Gson,Json Simple, ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        OauthToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue(response.getBody(), OauthToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        log.info("oauthToken.getAccess_token() => {} ", oauthToken.getAccess_token());
// ---------------------여기까지 토큰받아오기 이제 토큰으로 사용자 정보 받기.
        RestTemplate rt2 = new RestTemplate();

        //HttpHeader 오브젝트 생성
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers2);

        //Http요청하기 - post방식 - response변수의 응답을 받음
        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );
        log.info("response2->사용자 정보 json 상태 => {} ", response2.getBody());

        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper.readValue(response2.getBody(), KakaoProfile.class);
            log.info("kakao profile => {}", kakaoProfile.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoProfile;
    }

    // 소셜 로그인시 처음 들어온 회원 => true, 들어온적이 있는 회원 => false
    public Boolean isNew(String email) {
        Member memberByEmail = memberRepository.findMemberByEmail(email).orElse(null);
        return memberByEmail == null;
    }
}
