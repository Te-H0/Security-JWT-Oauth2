package me.teho.SecurityJwtOauth2.jwt;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Getter;
import me.teho.SecurityJwtOauth2.user.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@JsonIncludeProperties({"id", "createDate", "modifyDate", "username", "email", "authorities"})
public class MemberContext extends User {
    private final long id;
    private final LocalDateTime createDate;
    private final LocalDateTime modifyDate;
    private final String name;
    private final String email;
    private final Set<GrantedAuthority> authorities;

    public MemberContext(Member member) {
        super(member.getName(), "", member.getAuthorities());

        id = member.getId();
        createDate = member.getCreateDate();
        modifyDate = member.getModifyDate();
        name = member.getName();
        email = member.getEmail();
        authorities = member.getAuthorities().stream().collect(Collectors.toSet());
    }
}