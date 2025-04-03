package dding.customplace.dto.response;

import dding.customplace.entity.Markers;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class MarkerResponseDto {
    private String postId;
    private String title;
    private String userId;
    private String address;
    private String description;
    private LocalDateTime date;
    private Double latitude;
    private Double longitude;
    private String color;
    private int score;



    public static MarkerResponseDto of(Markers post) {
        return MarkerResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .address(post.getAddress())
                .description(post.getDescription())
                .date(post.getDate())
                .latitude(post.getLatitude())
                .longitude(post.getLongitude())
                .color(post.getColor())
                .score(post.getScore())

                .build();
    }
}
