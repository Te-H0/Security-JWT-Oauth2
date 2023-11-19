package me.teho.SecurityJwtOauth2;

import lombok.extern.slf4j.Slf4j;
import me.teho.SecurityJwtOauth2.jwt.JwtAccessDeniedHandler;
import me.teho.SecurityJwtOauth2.jwt.JwtAuthenticationEntryPoint;
import me.teho.SecurityJwtOauth2.jwt.JwtProvider;
import me.teho.SecurityJwtOauth2.jwt.JwtSecurityConfig;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;


    public SecurityConfig(JwtProvider jwtProvider, JwtAccessDeniedHandler jwtAccessDeniedHandler, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint
    ) {
        this.jwtProvider = jwtProvider;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                .requestMatchers(new AntPathRequestMatcher("/favicon.ico"));


        //h2-console 하위 요청, 파비콘 요청은 security 로직을 수행하지 않게 해줌.
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)

                )
                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests((registry) ->
                        registry.requestMatchers(
                                        new AntPathRequestMatcher("/login"),
                                        new AntPathRequestMatcher("/sign-in"),
                                        new AntPathRequestMatcher("/sign-up"),
                                        new AntPathRequestMatcher("/kakao/callback")).permitAll()
                                .requestMatchers(PathRequest.toH2Console()).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/admintest")).hasRole("ADMIN")

                                .anyRequest().authenticated()

                )

                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin
                        )
                )

//                .oauth2Login(
//                        oauth2Login -> oauth2Login
////                                .loginPage("/login")
//                                .successHandler(successHandler())
//                                .userInfoEndpoint(userInfoEndPoint -> userInfoEndPoint
//                                        .userService(oAuth2UserService))
//
//                )

                .apply(new JwtSecurityConfig(jwtProvider));
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        log.info("소셜로그인성공!");
        return ((request, response, authentication) -> {
            DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

            String id = defaultOAuth2User.getAttributes().get("id").toString();
            String body = """
                    {"id":"%s"}
                    """.formatted(id);

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());

            PrintWriter writer = response.getWriter();
            writer.println(body);
            writer.flush();
        });
    }
}
