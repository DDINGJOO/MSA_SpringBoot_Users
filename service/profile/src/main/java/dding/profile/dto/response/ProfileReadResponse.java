package dding.profile.dto.response;


import dding.profile.entity.Profile;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileReadResponse {
    private String nickname;
    private String city;
    private String preferred1;
    private String preferred2;
    private String introduction;

    public static ProfileReadResponse fromEntity(Profile profile) {
        return ProfileReadResponse.builder()
                .nickname(profile.getNickname())
                .city(profile.getCity())
                .preferred1(profile.getPreferred1())
                .preferred2(profile.getPreferred2())
                .introduction(profile.getIntroduction())
                .build();
    }
}
