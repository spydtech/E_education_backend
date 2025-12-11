package com.Eonline.Education.Service;

import com.Eonline.Education.Configuration.JwtTokenProvider;
import com.Eonline.Education.modals.Post;
import com.Eonline.Education.modals.User;
import com.Eonline.Education.repository.PostRepository;
import com.Eonline.Education.repository.SaveRepository;
import com.Eonline.Education.repository.UserRepository;
import com.Eonline.Education.response.PostResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private SaveRepository saveRepository;

    @Override
    public Post savePost(String jwt, List<MultipartFile> imageFiles, List<MultipartFile> videoFiles,
                         String name, String content, String postedBY, List<String> tags) {
        try {
            String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
            // ✅ FIXED: Use Optional properly
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found for email: " + email));

            Post post = new Post();
            post.setName(name);
            post.setContent(content);
            post.setPostedBY(user.getEmail());
            post.setTags(tags != null ? tags : new ArrayList<>());
            post.setLikeCount(0);
            post.setViewCount(0);
            post.setProfilePicture(user.getProfilePhoto());
            post.setCreatedAt(LocalDateTime.now());

            // Handle single image
            if (imageFiles != null && !imageFiles.isEmpty()) {
                MultipartFile imageFile = imageFiles.get(0);
                if (imageFile != null && !imageFile.isEmpty() && imageFile.getSize() > 0) {
                    post.setImg(imageFile.getBytes());
                    post.setMediaType(imageFile.getContentType());
                    post.setFileName(imageFile.getOriginalFilename());

                    // Also add to images list
                    post.getImages().add(imageFile.getBytes());
                }
            }

            // Handle single video (prioritize video over image)
            if (videoFiles != null && !videoFiles.isEmpty()) {
                MultipartFile videoFile = videoFiles.get(0);
                if (videoFile != null && !videoFile.isEmpty() && videoFile.getSize() > 0) {
                    post.setVideo(videoFile.getBytes());
                    post.setMediaType(videoFile.getContentType());
                    post.setFileName(videoFile.getOriginalFilename());

                    // Also add to videos list
                    post.getVideos().add(videoFile.getBytes());

                    // Clear image if video is present
                    post.setImg(null);
                    post.getImages().clear();
                }
            }

            return postRepository.save(post);

        } catch (IOException e) {
            throw new RuntimeException("Error processing media files: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error creating post: " + e.getMessage(), e);
        }
    }

    @Override
    public Post savePost(String jwt, MultipartFile file, String name, String content,
                         String postedBY, List<String> tags) throws IOException, SQLException {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        // ✅ FIXED: Use Optional properly
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + email));

        Post post = new Post();
        String contentType = file.getContentType();

        // Set the media type based on the file type
        if (contentType != null) {
            if (contentType.startsWith("image/")) {
                post.setImg(file.getBytes());
                post.setMediaType(contentType);
                post.setFileName(file.getOriginalFilename());
                post.getImages().add(file.getBytes());
            } else if (contentType.startsWith("video/")) {
                post.setVideo(file.getBytes());
                post.setMediaType(contentType);
                post.setFileName(file.getOriginalFilename());
                post.getVideos().add(file.getBytes());
            } else {
                throw new IllegalArgumentException("Unsupported media type: " + contentType);
            }
        }

        post.setName(name);
        post.setContent(content);
        post.setPostedBY(user.getEmail());
        post.setTags(tags != null ? tags : new ArrayList<>());
        post.setLikeCount(0);
        post.setViewCount(0);
        post.setCreatedAt(LocalDateTime.now());
        post.setProfilePicture(user.getProfilePhoto());

        return postRepository.save(post);
    }

    @Override
    public Post saveTextPost(String jwt, String name, String content, String postedBY, List<String> tags) {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        // ✅ FIXED: Use Optional properly
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + email));

        Post post = new Post(name, content, user.getEmail());
        post.setTags(tags != null ? tags : new ArrayList<>());
        post.setProfilePicture(user.getProfilePhoto());
        post.setLikeCount(0);
        post.setViewCount(0);

        return postRepository.save(post);
    }

    @Override
    public List<PostResponse> getAllPost() {
        List<Post> posts = postRepository.findAllOrderByCreatedAtDesc();
        List<PostResponse> responses = new ArrayList<>();

        for (Post post : posts) {
            PostResponse response = convertToResponse(post);
            // ✅ Already correct - Optional is handled properly here
            Optional<User> userOpt = userRepository.findByEmail(post.getPostedBY());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                response.setUserFullName(user.getFirstName() + " " + user.getLastName());
                response.setUserProfilePicture(user.getProfilePicture());
            }
            responses.add(response);
        }

        return responses;
    }

    @Override
    public Post getPostById(Long postId) {
        Optional<Post> optional = postRepository.findById(postId);
        if (optional.isPresent()) {
            Post post = optional.get();
            post.setViewCount(post.getViewCount() + 1);
            return postRepository.save(post);
        } else {
            throw new EntityNotFoundException("Post not found with id: " + postId);
        }
    }

    @Override
    public void likePost(String jwt, Long postId) {
        Optional<Post> optional = postRepository.findById(postId);
        if (optional.isPresent()) {
            Post post = optional.get();
            post.setLikeCount(post.getLikeCount() + 1);
            postRepository.save(post);
        } else {
            throw new EntityNotFoundException("Post not found with id: " + postId);
        }
    }

    @Override
    public List<Post> searchByName(String name) {
        return postRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public ResponseEntity<?> deletePostById(long id) {
        try {
            if (postRepository.existsById(id)) {
                saveRepository.deleteByPostId(id);
                postRepository.deleteById(id);
                return ResponseEntity.ok().body(Map.of("message", "Post deleted successfully"));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error: " + e.getMessage()));
        }
    }

    @Override
    public String saveProfilePhotoByEmail(String email, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("No file uploaded");
        }

        // ✅ FIXED: This already returns Optional, so it's fine
        Post post = postRepository.findByPostedBY(email)
                .orElseThrow(() -> new RuntimeException("Post not found for the given email: " + email));

        post.setProfilePicture(file.getBytes());
        postRepository.save(post);

        return "Profile photo uploaded successfully!";
    }

    @Override
    public void updateProfilePicture(long id, byte[] profilePicture) throws IOException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setProfilePicture(profilePicture);
        postRepository.save(post);
    }

    @Override
    public byte[] getProfilePicture(long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return post.getProfilePicture();
    }

    @Override
    public byte[] getImage(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return post.getImg();
    }

    @Override
    public byte[] getVideo(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return post.getVideo();
    }

    @Override
    public List<PostResponse> getUserPost(String jwt) {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        List<PostResponse> postResponses = new ArrayList<>();
        List<Post> posts = postRepository.findAllByPostedBY(email);

        for (Post post : posts) {
            postResponses.add(convertToResponse(post));
        }

        return postResponses;
    }

    @Override
    public Post updateTextPost(String jwt, Long postId, Post existingPost) {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        // ✅ FIXED: Use Optional properly
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + email));

        existingPost.setPostedBY(user.getEmail());
        existingPost.setProfilePicture(user.getProfilePhoto());
        existingPost.setUpdatedAt(LocalDateTime.now());

        return postRepository.save(existingPost);
    }

    @Override
    public Post updatePostWithMedia(String jwt, Long postId, MultipartFile file) throws IOException {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + postId));

        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        // ✅ FIXED: Use Optional properly
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + email));

        String contentType = file.getContentType();

        if (contentType != null) {
            if (contentType.startsWith("image/")) {
                existingPost.setImg(file.getBytes());
                existingPost.setMediaType(contentType);
                existingPost.setFileName(file.getOriginalFilename());
                existingPost.getImages().clear();
                existingPost.getImages().add(file.getBytes());
                // Clear video
                existingPost.setVideo(null);
                existingPost.getVideos().clear();
            } else if (contentType.startsWith("video/")) {
                existingPost.setVideo(file.getBytes());
                existingPost.setMediaType(contentType);
                existingPost.setFileName(file.getOriginalFilename());
                existingPost.getVideos().clear();
                existingPost.getVideos().add(file.getBytes());
                // Clear image
                existingPost.setImg(null);
                existingPost.getImages().clear();
            } else {
                throw new IllegalArgumentException("Unsupported media type: " + contentType);
            }
        }

        existingPost.setPostedBY(user.getEmail());
        existingPost.setProfilePicture(user.getProfilePhoto());
        existingPost.setUpdatedAt(LocalDateTime.now());

        return postRepository.save(existingPost);
    }

    // Helper method to convert Post to PostResponse
    private PostResponse convertToResponse(Post post) {
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setName(post.getName());
        response.setContent(post.getContent());
        response.setPostedBY(post.getPostedBY());
        response.setImg(post.getImg());
        response.setVideo(post.getVideo());
        response.setMediaType(post.getMediaType());
        response.setFileName(post.getFileName());
        response.setProfilePicture(post.getProfilePicture());
        response.setTags(post.getTags());
        response.setLikeCount(post.getLikeCount());
        response.setViewCount(post.getViewCount());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());

        return response;
    }
}