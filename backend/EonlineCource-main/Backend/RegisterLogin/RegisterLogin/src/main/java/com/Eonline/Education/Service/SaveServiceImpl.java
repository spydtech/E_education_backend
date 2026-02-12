package com.Eonline.Education.Service;

import java.util.List;
import java.util.Optional;

import com.Eonline.Education.modals.Post;
import com.Eonline.Education.modals.SaveEntity;
import com.Eonline.Education.repository.PostRepository;
import com.Eonline.Education.repository.SaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;



@Service
public class SaveServiceImpl implements SaveService {
	@Autowired
	SaveRepository saveRepository;
	@Autowired
	PostRepository postRepository;

	@Override
	public String savePostById(long id) {
		Optional<Post> findById = postRepository.findById(id);
		if(findById.isPresent()) {
			Post post = findById.get();
			SaveEntity saveE=new SaveEntity();
			saveE.setPost(post);
			saveRepository.save(saveE);
			return "Post saved successfully";
		}
		 throw new RuntimeException("post id is not found");
	}

	@Override
	public List<SaveEntity> getAllSavedPosts() {
		
		return saveRepository.findAll();
	}

	@Override
	public ResponseEntity<?> deleteSavedPost(int id) {

		try {
			if (saveRepository.existsById(id)) {
				saveRepository.deleteById(id);
				return ResponseEntity.ok().build();
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
		}
	}

}
