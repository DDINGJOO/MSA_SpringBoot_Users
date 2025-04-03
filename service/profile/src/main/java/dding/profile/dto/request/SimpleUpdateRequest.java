package dding.profile.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleUpdateRequest {
    private String nickname;
    private String phone;

    private String profileImageUrl;
    private String preferred1;
    private String preferred2;
    private String introduction;
    private boolean SNS_agree;
}
