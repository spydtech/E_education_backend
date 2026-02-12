package com.Eonline.Education.Controller;

import com.Eonline.Education.Configuration.JwtTokenProvider;
import com.Eonline.Education.Service.CommentService;
import com.Eonline.Education.modals.User;
import com.Eonline.Education.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/comments")
public class CommentController {
	@Autowired
	CommentService commentService;
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	@Autowired
	UserRepository userRepository;
	@PostMapping("/create")
	public ResponseEntity<?> createComment(@RequestHeader("Authorization") String jwt,@RequestParam long postId, @RequestParam String content){
		try {
			String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
			Optional<User> user = userRepository.findByEmail(email);
			return ResponseEntity.ok(commentService.createComment(postId, String.valueOf(user.getClass()), content));
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
