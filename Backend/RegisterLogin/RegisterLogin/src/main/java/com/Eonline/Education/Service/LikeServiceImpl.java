package com.Eonline.Education.Service;

import com.Eonline.Education.Configuration.JwtTokenProvider;
import com.Eonline.Education.modals.Like;
import com.Eonline.Education.modals.Post;
import com.Eonline.Education.modals.User;
import com.Eonline.Education.repository.LikeRepository;
import com.Eonline.Education.repository.PostRepository;
import com.Eonline.Education.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    @Override
    public int toggleLike(String jwt, Long postId) {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);

        // ✅ FIXED: Handle Optional properly
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));

        Optional<Like> existingLike = likeRepository.findByUserAndPost(user, post);

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get()); // Remove like if exists
        } else {
            Like newLike = new Like();
            newLike.setUser(user);
            newLike.setPost(post);
            likeRepository.save(newLike); // Add new like
        }

        // Update and return the correct like count
        int likeCount = likeRepository.countLikesByPostId(postId);
        post.setLikeCount(likeCount);
        postRepository.save(post);
        notificationService.createNotification(email, "post liked successfully");

        return likeCount; // Return updated like count
    }

    @Override
    public Map<String, Object> getLikeDetails(String jwt, Long postId) {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt); // Extract user email

        // ✅ FIXED: Handle Optional properly
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found for email: " + email));

        // Note: userId should be Long, not String based on your User entity
        Long userId = user.getId(); // Get user ID as Long

        // Check if user liked post - assuming this method exists in LikeRepository
        boolean isLiked = likeRepository.existsByUserAndPost(user, postRepository.findById(postId).orElse(null));

        // Alternative if you have a method that takes userId and postId
        // boolean isLiked = likeRepository.existsByUserIdAndPostId(userId, postId);

        int likeCount = likeRepository.countLikesByPostId(postId); // Get total like count

        Map<String, Object> response = new HashMap<>();
        response.put("isLiked", isLiked);
        response.put("likeCount", likeCount);
        response.put("userId", userId);

        return response;
    }

    @Override
    public int getLikeCount(Long postId) {
        return likeRepository.countLikesByPostId(postId);
    }
}