package com.Eonline.Education.Controller;

import com.Eonline.Education.Service.PostService;
import com.Eonline.Education.Service.SaveService;
import com.Eonline.Education.modals.Post;
import com.Eonline.Education.modals.SaveEntity;
import com.Eonline.Education.response.PostResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private SaveService saveService;

    // Create post with media
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPost(
            @RequestHeader("Authorization") String jwt,
            @RequestParam("name") String name,
            @RequestParam("content") String content,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            @RequestParam(value = "tags", required = false) List<String> tags) {

        try {
            // Extract email from token (remove "Bearer " prefix)
            String token = jwt.replace("Bearer ", "").trim();
            String email = token.contains("@") ? token : "user@example.com"; // Simplified

            Post createdPost;

            if (files == null || files.isEmpty()) {
                // Create text-only post
                createdPost = postService.saveTextPost(jwt, name, content, email, tags);
            } else {
                // Separate images and videos
                List<MultipartFile> imageFiles = new ArrayList<>();
                List<MultipartFile> videoFiles = new ArrayList<>();

                for (MultipartFile file : files) {
                    String contentType = file.getContentType();
                    if (contentType != null) {
                        if (contentType.startsWith("image/")) {
                            imageFiles.add(file);
                        } else if (contentType.startsWith("video/")) {
                            videoFiles.add(file);
                        } else {
                            return ResponseEntity.badRequest()
                                    .body(Map.of("error", "Invalid file type. Only images and videos are supported."));
                        }
                    }
                }

                // Create post with media
                createdPost = postService.savePost(jwt, imageFiles, videoFiles, name, content, email, tags);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Post created successfully");
            response.put("postId", createdPost.getId());
            response.put("post", createdPost);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "error", "Failed to create post: " + e.getMessage()
                    ));
        }
    }

    // Get all posts
    @GetMapping("/all")
    public ResponseEntity<?> getAllPosts() {
        try {
            List<PostResponse> posts = postService.getAllPost();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "posts", posts,
                    "count", posts.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "error", "Failed to fetch posts: " + e.getMessage()
                    ));
        }
    }

    // Get post by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        try {
            Post post = postService.getPostById(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "post", post
            ));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "success", false,
                            "error", "Post not found"
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "error", "Failed to fetch post: " + e.getMessage()
                    ));
        }
    }

    // Like a post
    @PostMapping("/{id}/like")
    public ResponseEntity<?> likePost(@RequestHeader("Authorization") String jwt,
                                      @PathVariable Long id) {
        try {
            postService.likePost(jwt, id);
            Post post = postService.getPostById(id);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Post liked successfully",
                    "likeCount", post.getLikeCount()
            ));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "success", false,
                            "error", "Post not found"
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "error", "Failed to like post: " + e.getMessage()
                    ));
        }
    }

    // Get image
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        try {
            byte[] image = postService.getImage(id);
            if (image == null || image.length == 0) {
                return ResponseEntity.notFound().build();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(image.length);
            headers.setCacheControl("max-age=3600");

            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get video
    @GetMapping("/{id}/video")
    public ResponseEntity<byte[]> getVideo(@PathVariable Long id) {
        try {
            byte[] video = postService.getVideo(id);
            if (video == null || video.length == 0) {
                return ResponseEntity.notFound().build();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentLength(video.length);
            headers.set("Content-Disposition", "inline; filename=\"video.mp4\"");
            headers.setCacheControl("max-age=3600");

            return new ResponseEntity<>(video, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Delete post
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        try {
            ResponseEntity<?> response = postService.deletePostById(id);
            if (response.getStatusCode() == HttpStatus.OK) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Post deleted successfully"
                ));
            } else {
                return response;
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "error", "Failed to delete post: " + e.getMessage()
                    ));
        }
    }

    // Get user posts
    @GetMapping("/user")
    public ResponseEntity<?> getUserPosts(@RequestHeader("Authorization") String jwt) {
        try {
            List<PostResponse> posts = postService.getUserPost(jwt);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "posts", posts,
                    "count", posts.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "error", "Failed to fetch user posts: " + e.getMessage()
                    ));
        }
    }

    // Search posts
    @GetMapping("/search")
    public ResponseEntity<?> searchPosts(@RequestParam("query") String query) {
        try {
            List<Post> posts = postService.searchByName(query);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "posts", posts,
                    "count", posts.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "error", "Failed to search posts: " + e.getMessage()
                    ));
        }
    }
}