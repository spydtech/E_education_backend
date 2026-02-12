package com.Eonline.Education.repository;

import com.Eonline.Education.modals.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByPostedBY(String email);

    Optional<Post> findByPostedBY(String email);

    List<Post> findByNameContainingIgnoreCase(String name);

    @Query("SELECT p FROM Post p ORDER BY p.createdAt DESC")
    List<Post> findAllOrderByCreatedAtDesc();

    @Query("SELECT p FROM Post p WHERE p.postedBY = :email ORDER BY p.createdAt DESC")
    List<Post> findUserPosts(@Param("email") String email);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.postedBY = :email")
    Long countByUserEmail(@Param("email") String email);
}