package com.Eonline.Education.Controller;

import com.Eonline.Education.Service.LikeService;
import com.Eonline.Education.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;
    @Autowired
    private LikeRepository likeRepository;

    @PutMapping("/{postId}")
    public ResponseEntity<Integer> likePost(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long postId
    ) {
        int updatedLikeCount = likeService.toggleLike(jwt, postId);
        return ResponseEntity.ok(updatedLikeCount); //  Returns updated like count
    }

    @GetMapping("/{postId}/details")
    public ResponseEntity<Map<String, Object>> getLikeDetails(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long postId
    ) {
        Map<String, Object> response = likeService.getLikeDetails(jwt, postId); //  Call service method
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{postId}/count")
    public ResponseEntity<Integer> getLikeCount(@PathVariable Long postId) {
        int likeCount = likeRepository.countLikesByPostId(postId); //  Get total like count
        return ResponseEntity.ok(likeCount);
    }

}
