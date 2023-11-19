package me.teho.SecurityJwtOauth2.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "Authority_id", referencedColumnName = "Authority_id")})
    private Set<Authority> authorities = new HashSet<>();

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
