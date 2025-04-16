package dding.profile.dto.request;


import dding.profile.entity.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter

@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {
    private String userId;
    private String nickname;
    private String email;
    private String phone;
    private String city;
    private String profileImageUrl;
    private String preferred1;
    private String preferred2;
    private String introduction;


    private Boolean SNS_agree;

}
