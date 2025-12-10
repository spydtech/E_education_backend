package com.Eonline.Education.repository;

import java.util.List;

import com.Eonline.Education.modals.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	List<Post> findAllBynameContaining(String name);

}
