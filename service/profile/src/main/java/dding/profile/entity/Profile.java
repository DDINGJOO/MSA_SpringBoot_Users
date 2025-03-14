package dding.profile.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_profile")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    private String userId;

    private String nickname;
    private String email;
    private String phone;

    private Long addressId;

    private String profileImageUrl;

    private String preferred1;  // 선호 1 (가장 잘하는 기량 등)
    private String preferred2;  // 선호 2 (보조 역할 등)

    @Column(length = 1000)
    private String introduction; // 자기소개 (간단하게 서술)

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
