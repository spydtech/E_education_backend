package com.Eonline.Education.Service;

import java.util.Map;

public interface LikeService {
    int toggleLike(String jwt, Long postId);
    int getLikeCount(Long postId);
    Map<String, Object> getLikeDetails(String jwt, Long postId);
}
