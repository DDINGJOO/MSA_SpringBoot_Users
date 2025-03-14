package dding.auth.entity;

import dding.auth.auth.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "auth")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auth {
    // 기본 정보
    @Id
    String id;             //Using ULID
    private String loginId;
    private String password;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private UserRole role;


    // 소셜 로그인
    private String provider;
    private String providerId;


}
