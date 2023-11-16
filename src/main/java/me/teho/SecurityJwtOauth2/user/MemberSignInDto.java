package me.teho.SecurityJwtOauth2.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignInDto {
    private String email;
    private String password;

    public static MemberSignInDto from(Member user) {
        if (user == null) return null;

        return MemberSignInDto.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
