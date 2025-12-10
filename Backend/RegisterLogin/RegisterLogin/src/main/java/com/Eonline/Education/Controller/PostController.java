package com.Eonline.Education.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.serial.SerialException;

import com.Eonline.Education.Service.PostService;
import com.Eonline.Education.Service.SaveService;
import com.Eonline.Education.modals.Post;
import com.Eonline.Education.modals.SaveEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/posts")
public class PostController {
	@Autowired
	private PostService postService;
	@Autowired
	SaveService saveService;
	@PostMapping("/createPost")
	public ResponseEntity<Post> createPost(@RequestBody Post post){
		try {
			Post createPost=postService.savePpost(post);
			
			return new  ResponseEntity<>(createPost,HttpStatus.CREATED);
		}catch(Exception e) {
			return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	@PostMapping("/createPost/image")
	public ResponseEntity<String> createPosts(@RequestParam MultipartFile file,@RequestParam String name,
			@RequestParam String content,@RequestParam String postedBY,@RequestParam List<String> tags)throws IOException, SerialException, SQLException{
		try {
			Post createPost=postService.savePost(file, name, content, postedBY, tags);
			//String notifications = postService.Notifications(file, name, content, postedBY, tags);
			
			return new  ResponseEntity<>("createdPost",HttpStatus.CREATED);
		}catch(Exception e) {
			return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
		
		
	}
	@GetMapping("/getPosts")
	public ResponseEntity<List<Post>> getAllPost(){
		
		try {
			return ResponseEntity.status(HttpStatus.OK).body(postService.getAllPost());
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			
		}
	}
	
	@GetMapping("/{postId}")
	public ResponseEntity<?> getPostById(@PathVariable long postId){
		try {
			Post post=postService.getPostById(postId);
			return ResponseEntity.ok(post);
		}catch(EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	@PutMapping("/{postId}/like")
	public ResponseEntity<?> likePost(@PathVariable long postId){
		try {
			postService.likePost(postId);
			return ResponseEntity.ok(new String[] {"Post liked successfully"});
		}catch(EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

		}
	}
	@GetMapping("/search/{name}")
	public ResponseEntity<?> searchByName(@PathVariable String name){
		try {
			return ResponseEntity.status(HttpStatus.OK).body(postService.searchByName(name));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	@DeleteMapping("/deletePost/{id}")
	public ResponseEntity<?> deletePostById(@PathVariable long id){
		try {
			return new ResponseEntity<>(postService.deletePostById(id),HttpStatus.OK);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

		}


	}
	@GetMapping("/getSavedPost/{id}")
	public ResponseEntity<String> savingThePost(@PathVariable long id){
		try {
			return new ResponseEntity<>(saveService.savePostById(id),HttpStatus.OK);
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			
		}
		
	}
	@GetMapping("getAllSavedPosts")
	public ResponseEntity<List<SaveEntity>> getAllSavedPost(){
		try {
			return new ResponseEntity<>(saveService.getAllSavedPosts(),HttpStatus.OK);
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			
		}
		
	}
	@DeleteMapping("deleteSavePost/{id}")
	public ResponseEntity<?> deleteSavedPost(@PathVariable int id){
		try {
			return new ResponseEntity<>(saveService.deleteSavedPost(id),HttpStatus.OK);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

		}
	}



}
