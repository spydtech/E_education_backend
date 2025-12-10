package com.Eonline.Education.Service;

import com.Eonline.Education.modals.Account;
import com.Eonline.Education.modals.Education;
import com.Eonline.Education.modals.PasswordChange;
import com.Eonline.Education.modals.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

	User findUserById(Long userId) throws Exception;

	User findUserProfileByJwt(String jwt) throws Exception;

	List<User> findAllUsers();
	List<User> getAllUsers();
	void deleteUser(Long userId);

	ResponseEntity<?> getAccountDetails(String email);

	ResponseEntity<?> updateAccountDetails(String email, Account userAccount);

	int generateSixDigitNumber();

	ResponseEntity<?> updateEducationDetails(String email, Education userEducation);

	ResponseEntity<?> getEducationDetails(String email);

	public User findByEmail(String email);

	public User saveUser(User user);

	public User updateDetails(long id,User userUpdate);
	// Method to get user by ID
	public User getUserById(Long userId);

	public String updatePassword(String email, PasswordChange passwordChange);



	public String saveProfilePhoto(String email, MultipartFile file) throws IOException;

	public String saveCoverPhoto(String email, MultipartFile file) throws IOException;

	public String saveFile(String email, MultipartFile file, String type) throws IOException;

	public byte[] getProfilePhoto(String email);

	public byte[] getCoverPhoto(String email);



}
