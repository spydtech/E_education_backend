package com.Eonline.Education.Service;

import com.Eonline.Education.modals.Comment;

import java.util.List;



public interface CommentService {
	Comment createComment(long postId, String postedBy, String content);
	List<Comment> getCommentsByPostId(Long postId);

}
