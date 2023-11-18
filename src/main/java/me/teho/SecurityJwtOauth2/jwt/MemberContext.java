package me.teho.SecurityJwtOauth2.jwt;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Getter;
import me.teho.SecurityJwtOauth2.user.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@JsonIncludeProperties({"id", "createDate", "modifyDate", "username", "email", "authorities"})
public class MemberContext extends User implements OAuth2User {
    private final long id;
    private final LocalDateTime createDate;
    private final LocalDateTime modifyDate;
    private final String name;
    private final String email;
    private final Set<GrantedAuthority> authorities;

    private Map<String, Object> attributes;
    private String userNameAttributeName;

    public MemberContext(Member member, List<GrantedAuthority> authorities) {
        super(member.getName(), member.getPassword(), authorities);

        id = member.getId();
        createDate = member.getCreateDate();
        modifyDate = member.getModifyDate();
        name = member.getName();
        email = member.getEmail();
        this.authorities = member.getAuthorities().stream().collect(Collectors.toSet());
    }

    public MemberContext(Member member, List<GrantedAuthority> authorities, Map<String, Object> attributes, String userNameAttributeName) {
        this(member, authorities);
        this.attributes = attributes;
        this.userNameAttributeName = userNameAttributeName;
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return super.getAuthorities().stream().collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getName() {
        return this.getAttribute(this.userNameAttributeName).toString();
    }

}