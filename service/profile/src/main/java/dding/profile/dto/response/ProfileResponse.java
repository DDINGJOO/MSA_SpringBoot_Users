package dding.profile.dto.response;


import dding.profile.entity.Profile;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponse {
    private String userId;
    private String nickname;
    private String email;
    private String phone;
    private String city;
    private String preferred1;
    private String preferred2;
    private String introduction;
    private Integer totalPoint;
    private Integer userLevel;

    public static ProfileResponse fromEntity(Profile profile) {
        return ProfileResponse.builder()
                .userId(profile.getUserId())
                .nickname(profile.getNickname())
                .email(profile.getEmail())
                .phone(profile.getPhone())
                .city(profile.getCity())
                .preferred1(profile.getPreferred1())
                .preferred2(profile.getPreferred2())
                .introduction(profile.getIntroduction())
                .totalPoint(profile.getTotalPoint())
                .userLevel(profile.getUserLevel())
                .build();
    }
}
