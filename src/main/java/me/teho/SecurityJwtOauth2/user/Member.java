package me.teho.SecurityJwtOauth2.user;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor

public class Member extends BaseEntity {


    private String name;
    private String email;
    private String password;
//    @Column(columnDefinition = "TEXT")
//    private String accessToken;

    // 현재 회원이 가지고 있는 권한들을 List<GrantedAuthority> 형태로 리턴
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("MEMBER"));

        return authorities;
    }

//    public Map<String, Object> getAccessTokenClaims() {
//        return Util.mapOf(
//                "id", getId(),
//                "createDate", getCreateDate(),
//                "modifyDate", getModifyDate(),
//                "username", getName(),
//                "email", getEmail(),
//                "authorities", getAuthorities()
//        );
//    }

//    public Map<String, Object> toMap() {
//        return Util.mapOf(
//                "id", getId(),
//                "createDate", getCreateDate(),
//                "modifyDate", getModifyDate(),
//                "username", getName(),
//                "email", getEmail(),
//                "accessToken", getAccessToken(),
//                "authorities", getAuthorities()
//        );
//    }
}
