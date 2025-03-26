package dding.auth.dto.kakao;

import lombok.Data;

@Data
public class KakaoRequestDto {
    private String code;
    private String redirectUri;

    // Getter & Setter 또는 @Data
}
