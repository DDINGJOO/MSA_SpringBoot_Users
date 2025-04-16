package dding.profile.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    private String userId; // FK - users.id

    private String nickname;
    private String email;

    private String phone;

    private String City;

    private String profileImageUrl; // FileUpload 모듈에서 받은 이미지 URL

    private String preferred1; // 선호 1 (가장 잘하는 기량 등)

    private String preferred2; // 선호 2 (보조 역할 등)

    @Column(length = 1000)
    private String introduction; // 자기소개

    private boolean snsAgree;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // 향후 확장 대비 (포인트/레벨 저장 가능)
    private Integer totalPoint;
    private Integer userLevel;


    public void SimpleProfile(String nickname, String preferred1, String preferred2, String profileImageUrl,
                              String phone, String introduction, boolean snsAgree)
    {
        this.profileImageUrl = profileImageUrl;
        this.preferred2  = preferred2;
        this.preferred1 = preferred1;
        this.introduction = introduction;
        this.phone = phone;
        this.nickname = nickname;
        this.snsAgree = snsAgree;
    }

}
