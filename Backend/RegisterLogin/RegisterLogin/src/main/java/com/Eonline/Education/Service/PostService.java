package com.Eonline.Education.Service;

import com.Eonline.Education.modals.Post;
import com.Eonline.Education.response.PostResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface PostService {

	List<PostResponse> getAllPost();

	Post getPostById(Long postId);

	void likePost(String jwt,Long postId);

	List<Post> searchByName(String name);

	ResponseEntity<?> deletePostById(long id);

	Post saveTextPost(String jwt,String name, String content, String postedBY, List<String> tags);

	Post savePost(String jwt,MultipartFile file, String name, String content, String postedBY, List<String> tags)
			throws IOException, SQLException;

	String saveProfilePhotoByEmail(String email, MultipartFile file) throws IOException;

	// Method to update profile picture for a post
	void updateProfilePicture(long id, byte[] profilePicture) throws IOException;

	// Method to retrieve profile picture of a post
	byte[] getProfilePicture(long id);


	byte[] getImage(Long postId);

	byte[] getVideo(Long postId);

	List<PostResponse> getUserPost(String jwt);
	public Post updateTextPost(String jwt, Long postId, Post existingPost);
	public Post updatePostWithMedia(String jwt, Long postId, MultipartFile file) throws IOException;

    Post savePost(String jwt, List<MultipartFile> imageFiles, List<MultipartFile> videoFiles, String name, String content, String postedBY, List<String> tags);
}