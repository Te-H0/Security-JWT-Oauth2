package me.teho.SecurityJwtOauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.teho.SecurityJwtOauth2.jwt.MemberContext;
import me.teho.SecurityJwtOauth2.user.Member;
import me.teho.SecurityJwtOauth2.user.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private MemberRepository memberRepository;


    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("이거 좀 제발 되라!!!!!!!!!!");
        log.info("kakao user load!!!");
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                .getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String oauthId = oAuth2User.getName();

        Member member = null;
        String oauthType = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

//        if (!"KAKAO".equals(oauthType)) {
//            throw new OAuthTypeMatchNotFoundException();
//        }

        if (isNew(oauthType, oauthId)) {
            switch (oauthType) {
                case "KAKAO" -> {
                    log.debug("attributes : " + attributes);

                    Map attributesProperties = (Map) attributes.get("properties");
                    Map attributesKakaoAcount = (Map) attributes.get("kakao_account");
                    String nickname = (String) attributesProperties.get("nickname");
                    String profile_image = (String) attributesProperties.get("profile_image");
                    String email = "%s@kakao.com".formatted(oauthId);
                    String username = "KAKAO_%s".formatted(oauthId);

                    if ((boolean) attributesKakaoAcount.get("has_email")) {
                        email = (String) attributesKakaoAcount.get("email");
                    }

                    member = Member.builder()
                            .email(email)
                            .name(username)
                            .password("")
                            .build();

                    memberRepository.save(member);

                    //memberService.setProfileImgByUrl(member, profile_image);
                }
            }
        } else {
            member = memberRepository.findMemberByName("%s_%s".formatted(oauthType, oauthId))
                    .orElseThrow(MemberNotFoundException::new);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("member"));
        return new MemberContext(member, authorities, attributes, userNameAttributeName);

    }

    private boolean isNew(String oAuthType, String oAuthId) {
        return memberRepository.findMemberByName("%s_%s".formatted(oAuthType, oAuthId)).isEmpty();
    }
}