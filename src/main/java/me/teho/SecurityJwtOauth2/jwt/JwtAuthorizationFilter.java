package me.teho.SecurityJwtOauth2.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    private final JwtProvider jwtProvider;
    private final String AUTHORIZE_HEADER = "Authorization";


    public JwtAuthorizationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("요청 url in filter => {}", request.getRequestURI());
        String bearerToken = request.getHeader("Authorization");
        String requestURI = request.getRequestURI();
        log.info("!!!!!!! TOKEN => {}", bearerToken);

        if (bearerToken != null) {
            String token = bearerToken.substring("Bearer ".length());
            log.info("1");
            log.info("jwt token =>{}", token);
            // 1차 체크(정보가 변조되지 않았는지 체크)
            if (jwtProvider.verify(token)) {
                Authentication authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
            } else {
                log.info("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);

//                // Token에서 사용자 정보 꺼내기
//                String memberEmail = "";
//
//                //권한 부여
//                UsernamePasswordAuthenticationToken authenticationToken =
//                        new UsernamePasswordAuthenticationToken(memberEmail, null, List.of(new SimpleGrantedAuthority("MEMBER"))); // ---------------------2
//
//                // request를 넣어 detail을 빌드하고 추가한다.
//                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        log.info("넘어가좀");
        filterChain.doFilter(request, response);
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        System.out.println("accessHeader => " + AUTHORIZE_HEADER);
        log.info("이거니?");
        log.info("header이름 => {}", AUTHORIZE_HEADER);
        String bearerToken = request.getHeader(AUTHORIZE_HEADER);
        log.info("headr => {}", bearerToken);
        return Optional.ofNullable(request.getHeader(AUTHORIZE_HEADER));

    }

//    private void forceAuthentication(Member member) {
//        MemberContext memberContext = new MemberContext(member);
//
//        UsernamePasswordAuthenticationToken authentication =
//                UsernamePasswordAuthenticationToken.authenticated(
//                        memberContext,
//                        null,
//                        member.getAuthorities()
//                );
//
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//        context.setAuthentication(authentication);
//        SecurityContextHolder.setContext(context);
//    }
}
