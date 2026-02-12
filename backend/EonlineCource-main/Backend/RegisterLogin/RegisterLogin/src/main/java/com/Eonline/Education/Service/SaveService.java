package com.Eonline.Education.Service;

import com.Eonline.Education.modals.SaveEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface SaveService {
	String savePostById(long id);
	public List<SaveEntity> getAllSavedPosts();
	ResponseEntity<?> deleteSavedPost(int id);
}
