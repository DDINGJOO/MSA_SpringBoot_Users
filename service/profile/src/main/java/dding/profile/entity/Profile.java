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

    public boolean isSnsAgree() {
        return snsAgree;
    }




}
