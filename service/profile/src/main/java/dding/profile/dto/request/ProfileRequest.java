package dding.profile.dto.request;


import dding.profile.entity.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {
    private String userId;
    private String nickname;
    private String email;
    private String phone;
    private String city;
    private String preferred1;
    private String preferred2;
    private String introduction;


    private Boolean SNS_agree;

    public static Profile toEntity(ProfileRequest profileRequest) {
        return Profile.builder()
                .userId(profileRequest.getUserId())
                .nickname(profileRequest.getNickname())
                .email(profileRequest.getEmail())
                .phone(profileRequest.getPhone())
                .City(profileRequest.getCity())
                .preferred1(profileRequest.getPreferred1())
                .preferred2(profileRequest.getPreferred2())
                .introduction(profileRequest.getIntroduction())
                .snsAgree(profileRequest.getSNS_agree())
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

}
