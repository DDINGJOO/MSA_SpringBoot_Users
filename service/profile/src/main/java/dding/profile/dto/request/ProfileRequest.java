package dding.profile.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {
    private String userId;
    private String nickname;
    private String email;
    private String phone;
    private Long addressId;
    private String profileImageUrl;
    private String preferred1;
    private String preferred2;
    private String introduction;
}
