package com.Eonline.Education.Controller;

import com.Eonline.Education.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/comments")
public class CommentController {
	@Autowired
	CommentService commentService;
	@PostMapping("/create")
	public ResponseEntity<?> createComment(@RequestParam long postId,@RequestParam String postedBy,@RequestParam String content){
		try {
			return ResponseEntity.ok(commentService.createComment(postId, postedBy, content));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
		}
	}
	@GetMapping("/{postId}")
	public ResponseEntity<?> getCommentsByPostId(@PathVariable long postId){
		try {
			return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
		}
	}
 
}
