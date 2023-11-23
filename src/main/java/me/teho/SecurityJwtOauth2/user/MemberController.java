package me.teho.SecurityJwtOauth2.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    @Value("${jwt.header}")
    private String accessHeader;

    // 현재는 필요 없음. 로컬 로그인 생길 시 사용.
    @PostMapping("/sign-up")
    public ResponseEntity<MemberSignUpDto> join(@Valid @RequestBody MemberSignUpDto userSignUpDto) {
        MemberSignUpDto joinMember = memberService.join(userSignUpDto);
        log.info("JoinMember => {}", joinMember.toString());
        return ResponseEntity.ok().body(joinMember);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> loin(@Valid @RequestBody MemberSignInDto memberSignInDto) {
        String jwt = memberService.login(memberSignInDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(accessHeader, "Bearer " + jwt);
        return new ResponseEntity<>(jwt, httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> test() {
        log.info("이거안들어와?");

        return ResponseEntity.ok().body("SUCCESS!!!");
    }

    @GetMapping("/hell2")
    public String test2(HttpServletRequest request) {
        log.info("이거안들어와?");
        String bearerToken = request.getHeader("Authorization");
        String token = bearerToken.substring("Bearer ".length());
        log.info("token => {}", token);

        return "SUCESSSSSS~~~~~~~";
    }

    @GetMapping("/admintest")
    public ResponseEntity<String> roleTest() {
        log.info("이거안들어와?");

        return ResponseEntity.ok().body("SUCCESS~~");
    }

    @GetMapping("/ngrok-test")
    public String ngrok() {
        log.info("이거안들어와?");

        return "연동 성공";
    }
}
