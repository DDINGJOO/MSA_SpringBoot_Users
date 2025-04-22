package dding.profile.dto.response;

import dding.profile.entity.Profile;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileSimpleResponse {
    private String userId;
    private String nickname;
    private String preferred1;
    private String preferred2;
    private String city;


    public static ProfileSimpleResponse fromEntity(Profile profile)
    {
        return ProfileSimpleResponse.builder()
                .userId(profile.getUserId())
                .nickname(profile.getNickname())
                .preferred1(profile.getPreferred1())
                .preferred2(profile.getPreferred2())
                .city(profile.getCity())
                .build();
    }
}
