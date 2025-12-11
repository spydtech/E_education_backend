package com.Eonline.Education.repository;

import com.Eonline.Education.modals.Like;
import com.Eonline.Education.modals.Post;
import com.Eonline.Education.modals.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);
    boolean existsByUserIdAndPostId(String userId, Long postId); //  Check if user liked post

    int countLikesByPostId(Long postId);

    boolean existsByUserAndPost(User user, Post post);
}

