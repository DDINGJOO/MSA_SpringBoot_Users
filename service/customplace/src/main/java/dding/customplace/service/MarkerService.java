package dding.customplace.service;


import dding.customplace.dto.request.MarkerCreatedRequest;
import dding.customplace.dto.response.MarkerResponseDto;
import dding.customplace.dto.response.MarkerSimpleResponseDto;
import dding.customplace.entity.Markers;
import dding.customplace.repostiory.MarkersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MarkerService {

    private final MarkersRepository markersRepository;


    @Transactional
    public MarkerResponseDto createPost(MarkerCreatedRequest req) {
        // 1. Post 저장

        Markers post = Markers.builder()
                .id(req.getPostId())
                .title(req.getTitle())
                .address(req.getAddress())
                .userId(req.getUserId())
                .description(req.getDescription())
                .date(req.getDate())
                .latitude(req.getLatitude())
                .longitude(req.getLongitude())
                .color(req.getColor())
                .score(req.getScore())
                .build();
        Markers savedPost = markersRepository.save(post);


        // 3. DTO로 변환 후 반환
        return MarkerResponseDto.of(savedPost);
    }

    @Transactional(readOnly = true)
    public List<MarkerResponseDto> getAllPosts() {
        return markersRepository.findAll().stream()
                .map(MarkerResponseDto::of)
                .collect(Collectors.toList());
    }



    public List<MarkerSimpleResponseDto> getMyPosts(String userId) {
        return markersRepository.findAllByUserId(userId).stream()
                .map(MarkerSimpleResponseDto::of)
                .collect(Collectors.toList());
    }

    public MarkerSimpleResponseDto getSimplePostById(String articleId) {
        Markers marker =  markersRepository.findById(articleId).orElseThrow();
        return MarkerSimpleResponseDto.of(marker);
    }
}
