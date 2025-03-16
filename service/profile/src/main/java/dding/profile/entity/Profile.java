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
    private String userId; // FK - users.id

    private String nickname;

    private String email;

    private String phone;

    private Long addressId; // FK - address.id (Address 모듈 연동)

    private String profileImageUrl; // FileUpload 모듈에서 받은 이미지 URL

    private String preferred1; // 선호 1 (가장 잘하는 기량 등)

    private String preferred2; // 선호 2 (보조 역할 등)

    @Column(length = 1000)
    private String introduction; // 자기소개

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // 향후 확장 대비 (포인트/레벨 저장 가능)
    private Integer totalPoint;
    private Integer userLevel;

    public void update(String nickname, String email, String phone, Long addressId,
                       String profileImageUrl, String preferred1, String preferred2, String introduction) {

        this.addressId = addressId;
        this.nickname = nickname;
        this.email =email;
        this.phone = phone;
        this.introduction = introduction;
        this.preferred1 = preferred1;
        this.preferred2 = preferred2;
        this.profileImageUrl = profileImageUrl;
    }
}
