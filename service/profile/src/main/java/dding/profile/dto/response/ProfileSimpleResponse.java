package dding.profile.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileSimpleResponse {
    private String userId;
    private String nickname;
    private String profileImageUrl;
    private String preferred1;
    private String preferred2;
    private String city;
}
