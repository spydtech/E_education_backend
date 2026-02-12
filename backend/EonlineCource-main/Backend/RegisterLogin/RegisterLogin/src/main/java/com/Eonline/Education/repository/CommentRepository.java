package com.Eonline.Education.repository;

import java.util.List;

import com.Eonline.Education.modals.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findByPostId(long postId);

}
