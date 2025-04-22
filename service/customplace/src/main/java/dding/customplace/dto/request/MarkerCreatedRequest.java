package dding.customplace.dto.request;

import dding.customplace.entity.Markers;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class MarkerCreatedRequest {

    private String postId;
    private String title;
    private String address;
    private String userId;
    private String description;
    private LocalDateTime date;
    private Double latitude;
    private Double longitude;
    private String color;
    private int score;

    public static Markers toEntity(MarkerCreatedRequest request) {
        return Markers.builder()
                .id(request.getPostId())
                .title(request.getTitle())
                .address(request.getAddress())
                .userId(request.getUserId())
                .description(request.getDescription())
                .date(request.getDate())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .color(request.getColor())
                .score(request.getScore())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }

}
