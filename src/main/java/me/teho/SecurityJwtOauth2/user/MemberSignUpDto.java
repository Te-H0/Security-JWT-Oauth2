package me.teho.SecurityJwtOauth2.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberSignUpDto {
    @NotEmpty
    private String name;
    private String email;
    private String password;

    public static MemberSignUpDto from(Member user) {
        if (user == null) return null;

        return MemberSignUpDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
