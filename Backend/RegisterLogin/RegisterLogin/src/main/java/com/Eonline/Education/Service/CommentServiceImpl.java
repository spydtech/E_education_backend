package com.Eonline.Education.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.Eonline.Education.modals.Comment;
import com.Eonline.Education.modals.Post;
import com.Eonline.Education.repository.CommentRepository;
import com.Eonline.Education.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CommentServiceImpl  implements CommentService{
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private PostRepository postRepository;
	public Comment createComment(long postId, String postedBy, String content) {
		Optional<Post> optional = postRepository.findById(postId);
		if(optional.isPresent()) {
			Comment comment=new Comment();
			comment.setPost(optional.get());
			comment.setContent(content);
			comment.setPostedBy(postedBy);
		    comment.setCreatedAt(LocalDateTime.now());
			return commentRepository.save(comment);
		}
		throw new EntityNotFoundException("Post not found");
		
	}
	public List<Comment> getCommentsByPostId(Long postId){
		return commentRepository.findByPostId(postId);
		
	}
	

}
