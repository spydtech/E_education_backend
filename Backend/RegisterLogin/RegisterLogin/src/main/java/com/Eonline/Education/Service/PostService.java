package com.Eonline.Education.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.serial.SerialException;

import com.Eonline.Education.modals.Post;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;



public interface PostService {
	Post savePpost(Post post);
	List<Post> getAllPost();
	Post getPostById(Long postId);
	void likePost(Long postId);
	List<Post> searchByName(String name);
	ResponseEntity<?> deletePostById(long id);
	Post savePost(MultipartFile file,String name,String content,String postedBY,List<String> tags) throws SerialException, SQLException, IOException;
	String Notifications(MultipartFile file,String name,String content,String postedBY,List<String> tags) throws SerialException, SQLException, IOException;
}
