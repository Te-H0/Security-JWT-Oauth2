package me.teho.SecurityJwtOauth2.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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

    /*
   @ElementCollection : 컬렉션의 각 요소를 저장할 수 있다. 부모 Entity와 독립적으로 사용 X
   @CollectionTable : @ElementCollection과 함께 사용될 때, 생성될 테이블의 이름 지정
   */
    @ElementCollection(targetClass = MemberRole.class)
    @CollectionTable(name = "member_roles",
            joinColumns = @JoinColumn(name = "member_id"))
    @Enumerated(EnumType.STRING)
    private Set<MemberRole> roleSet;


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
