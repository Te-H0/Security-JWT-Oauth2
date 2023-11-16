package me.teho.SecurityJwtOauth2.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private JwtProvider jwtProvider;


    public JwtSecurityConfig(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;

    }

    // JwtFilter를 통해 Security로직에 필터 등록
    @Override
    public void configure(HttpSecurity http) {
        http.addFilterBefore(
                new JwtAuthorizationFilter(jwtProvider),
                UsernamePasswordAuthenticationFilter.class
        );
    }
}


//@Configuration
//public class JwtSecurityConfig {
//    @Value("${jwt.secret}")
//    private String secretKeyPlain;
//
//    @Bean
//    public SecretKey jwtSecretKey() {
//        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKeyPlain.getBytes());
//        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
//    }
//}
