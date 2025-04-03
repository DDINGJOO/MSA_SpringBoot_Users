package dding.customplace.controller;


import dding.customplace.dto.request.MarkerCreatedRequest;
import dding.customplace.dto.response.MarkerResponseDto;
import dding.customplace.dto.response.MarkerSimpleResponseDto;
import dding.customplace.service.MarkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/markers")
@RequiredArgsConstructor
public class MarkersController {
    private final MarkerService markerService;


    @PostMapping
    public ResponseEntity<MarkerResponseDto> createPost(@RequestBody MarkerCreatedRequest requestDto) {
        MarkerResponseDto saved = markerService.createPost(requestDto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MarkerResponseDto>> getAllPosts() {
        List<MarkerResponseDto> posts = markerService.getAllPosts();
        return ResponseEntity.ok(posts);
    }


    @GetMapping("/simple/{Id}")
    public ResponseEntity<MarkerSimpleResponseDto> getPost(@PathVariable(name = "Id") String articleId) {
        MarkerSimpleResponseDto post = markerService.getSimplePostById(articleId);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/simple/my/{userId}")
    public ResponseEntity<List<MarkerSimpleResponseDto>> getSimpleMyPosts(@PathVariable(name = "userId") String userId){

        List<MarkerSimpleResponseDto> posts = markerService.getMyPosts(userId);
        return ResponseEntity.ok(posts);
    }




}
