package dding.auth.dto.request;

import dding.auth.auth.UserRole;
import dding.auth.entity.Auth;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pk.ULID;

@Getter
@Setter
@NoArgsConstructor
public class JoinRequest {

    private String loginId;
    private String password;
    private String passwordCheck;
    private String nickname;




    public Auth toEntity(String encodedPassword){
        return Auth.builder()
                .id(new ULID().generatedKey())
                .loginId(this.loginId)
                .password(encodedPassword)
                .nickname(this.nickname)
                .role(UserRole.USER)
                .build();
    }
}


