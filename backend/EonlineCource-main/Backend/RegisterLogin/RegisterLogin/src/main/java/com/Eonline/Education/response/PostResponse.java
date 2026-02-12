package com.Eonline.Education.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponse {
    private Long id;
    private String name;
    private String content;
    private String postedBY;
    private byte[] img;
    private byte[] video;
    private String mediaType;
    private String fileName;
    private byte[] profilePicture;
    private List<String> tags;
    private int likeCount;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isLiked;
    private int commentCount;
    private String userProfilePicture;
    private String userFullName;
}