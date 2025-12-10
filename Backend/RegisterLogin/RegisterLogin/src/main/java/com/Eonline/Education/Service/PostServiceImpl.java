package com.Eonline.Education.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialException;

import com.Eonline.Education.modals.Post;
import com.Eonline.Education.repository.CommentRepository;
import com.Eonline.Education.repository.PostRepository;
import com.Eonline.Education.repository.SaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.sql.Blob;

import jakarta.persistence.EntityNotFoundException;


@Service
public class PostServiceImpl implements PostService {
	@Autowired
	PostRepository postRepository;
	@Autowired
	CommentRepository commentRepository;
	@Autowired
	SaveRepository saveRepository;
	
	public Post savePpost(Post post) {
		post.setLikeCount(0);
		post.setViewCount(0);
		
		 
		
		post.setDateTime(LocalDateTime.now());
		postRepository.save(post);
		return post;
		
	}
	public Post savePost(MultipartFile file,String name,String content,String postedBY,List<String> tags) throws SerialException, SQLException, IOException {
		Post post=new Post();
//		Blob blob=new javax.sql.rowset.serial.SerialBlob(file.getBytes());
//		post.setImg(blob);
		post.setImg(file.getBytes());
		post.setName(name);
		post.setContent(content);
		post.setPostedBY(postedBY);
		post.setLikeCount(0);
		post.setViewCount(0);
		post.setDateTime(LocalDateTime.now());
		post.setTags(tags);
		postRepository.save(post);
		return post;
		
		
	}
	public List<Post> getAllPost(){
		return postRepository.findAll();
		
	}
	public Post getPostById(Long postId) {
		Optional<Post> optional = postRepository.findById(postId);
		if(optional.isPresent()) {
			Post post=optional.get();
			post.setViewCount(post.getViewCount()+1);
			return  postRepository.save(post);
		}else { 
			throw new EntityNotFoundException("Post not found");
		}
	}
	
	public void likePost(Long postId) {
		Optional<Post> optional = postRepository.findById(postId);
		if(optional.isPresent()) {
			Post post=optional.get();
			post.setLikeCount(post.getLikeCount()+1);
			postRepository.save(post);
		}else {
			throw new EntityNotFoundException("Post not found with id"+ postId);
		}
		
	}
	public List<Post> searchByName(String name){
		return postRepository.findAllBynameContaining(name);
	}
	@Override
	public String Notifications(MultipartFile file, String name, String content, String postedBY, List<String> tags)
			throws SerialException, SQLException, IOException {
		
		
		return content+ "updated successfully by" + postedBY;
	}
	@Override
	public ResponseEntity<?> deletePostById(long id) {

		try {
			if (postRepository.existsById(id)) {
				saveRepository.deleteByPostId(id);
				postRepository.deleteById(id);
				return ResponseEntity.ok().build();
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
		}
	}
	

}
