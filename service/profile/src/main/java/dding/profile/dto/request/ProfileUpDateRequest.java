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
public class ProfileUpDateRequest {
    private String nickname;
    private String phone;
    private String city;


    private String preferred1;
    private String preferred2;
    private String introduction;
    private boolean SNS_agree;
    public boolean isSnsAgree() {
        return SNS_agree;
    }

    public static Profile ToEntity(ProfileUpDateRequest request, Profile profile) {
        profile.setCity(request.getCity());
        profile.setNickname(request.getNickname());
        profile.setPhone(request.getPhone());
        profile.setPreferred1(request.getPreferred1());
        profile.setPreferred2(request.getPreferred2());
        profile.setIntroduction(request.getIntroduction());
        profile.setSnsAgree(request.isSNS_agree());
        profile.setUpdatedAt(LocalDateTime.now());
        return profile;

    }
}
