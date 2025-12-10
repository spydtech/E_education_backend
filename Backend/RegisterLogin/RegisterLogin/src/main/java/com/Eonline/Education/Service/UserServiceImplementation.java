package com.Eonline.Education.Service;

import com.Eonline.Education.Configuration.JwtTokenProvider;
import com.Eonline.Education.exceptions.UserException;
import com.Eonline.Education.modals.Account;
import com.Eonline.Education.modals.Education;
import com.Eonline.Education.modals.PasswordChange;
import com.Eonline.Education.modals.User;
import com.Eonline.Education.repository.AccountRepository;
import com.Eonline.Education.repository.EducationRepository;
import com.Eonline.Education.repository.UserRepository;
import com.Eonline.Education.response.AccountResponse;
import com.Eonline.Education.response.EducationResponse;
import com.Eonline.Education.response.MessageResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AccountRepository accountRepository;
    private final EducationRepository educationRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, AccountRepository
            accountRepository, EducationRepository educationRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.accountRepository = accountRepository;
        this.educationRepository = educationRepository;
        this.passwordEncoder=passwordEncoder;
    }

    @Override
    public User findUserById(Long userId) throws UserException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User not found with id " + userId));
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("User not exist with email " + email);
        }
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAllByOrderByCreatedAtDesc();
    }

//    @Override
//    public List<User> getAllUsers() {
//        return null;
//    }

    @Override
    public void deleteUser(Long userId) {

    }

    @Override
    public ResponseEntity<?> getAccountDetails(String email) {
        User userDetails = userRepository.findByEmail(email);
        long userAccID = userDetails.getId();

        if (!accountRepository.existsByUserId(userAccID)) {
            Account accountDetails = new Account();
            accountDetails.setUser(userDetails);
            accountDetails.setUserEmail(userDetails.getEmail());
            accountDetails.setFullName(userDetails.getFirstName() + " " + userDetails.getLastName());
            accountRepository.save(accountDetails);
        }

        Account account = accountRepository.findByUserId(userAccID);
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setFullName(account.getFullName());
        accountResponse.setUserEmail(account.getUserEmail());
        accountResponse.setLocation(account.getLocation());
        accountResponse.setPhoneNumber(account.getPhoneNumber());

        return ResponseEntity.ok(accountResponse);
    }

    @Override
    public ResponseEntity<?> updateAccountDetails(String email, Account userAccount) {
        User currentUser = userRepository.findByEmail(email);
        long currentUserId = currentUser.getId();

        if (accountRepository.existsByUserId(currentUserId)) {
            Account currentAccountDetails = accountRepository.findByUserId(currentUserId);
            currentAccountDetails.setFullName(userAccount.getFullName());
            currentAccountDetails.setPhoneNumber(userAccount.getPhoneNumber());
            currentAccountDetails.setLocation(userAccount.getLocation());

            if (!accountRepository.existsByUserEmail(userAccount.getUserEmail())) {
                currentUser.setEmail(userAccount.getUserEmail());
                currentAccountDetails.setUserEmail(userAccount.getUserEmail());
            }

            accountRepository.save(currentAccountDetails);
            userRepository.save(currentUser);

            return getAccountDetails(userAccount.getUserEmail());
        } else {
            return ResponseEntity.ok(new MessageResponse("Email already exists, please enter a new email ID"));
        }
    }

    @Override
    public int generateSixDigitNumber() {
        Random random = new Random();
        return random.nextInt(900000) + 100000;
    }



    @Override
    public ResponseEntity<?> updateEducationDetails(String email, Education userEducation) {
        User currentEduUser = userRepository.findByEmail(email);
        long currentEduUserId = currentEduUser.getId();

        if (educationRepository.existsByUserId(currentEduUserId)) {
            Education currentEduDetails = educationRepository.findByUserId(currentEduUserId);
            currentEduDetails.setNameOfInstitution(userEducation.getNameOfInstitution());
            currentEduDetails.setDegree(userEducation.getDegree());
            currentEduDetails.setStartMonth(userEducation.getStartMonth());
            currentEduDetails.setStartYear(userEducation.getStartYear());
            currentEduDetails.setGraduationMonth(userEducation.getGraduationMonth());
            currentEduDetails.setGraduationYear(userEducation.getGraduationYear());
            educationRepository.save(currentEduDetails);
        } else {
            Education currentEduDetailsNew = new Education();
            currentEduDetailsNew.setUser(currentEduUser);
            currentEduDetailsNew.setNameOfInstitution(userEducation.getNameOfInstitution());
            currentEduDetailsNew.setDegree(userEducation.getDegree());
            currentEduDetailsNew.setStartMonth(userEducation.getStartMonth());
            currentEduDetailsNew.setStartYear(userEducation.getStartYear());
            currentEduDetailsNew.setGraduationMonth(userEducation.getGraduationMonth());
            currentEduDetailsNew.setGraduationYear(userEducation.getGraduationYear());
            educationRepository.save(currentEduDetailsNew);
        }

        return getEducationDetails(email);
    }

    @Override
    public ResponseEntity<?> getEducationDetails(String email) {
        User userEducationDetails = userRepository.findByEmail(email);
        long userEducationID = userEducationDetails.getId();
        Education education = educationRepository.findByUserId(userEducationID);

        if (education != null) {
            EducationResponse educationResponse = getEducationResponse(education);
            return ResponseEntity.ok(educationResponse);
        } else {
            MessageResponse messageResponse = new MessageResponse("Education details are not updated");
            return ResponseEntity.ok(messageResponse);
        }
    }

    private static @NotNull EducationResponse getEducationResponse(Education education) {
        EducationResponse educationResponse = new EducationResponse();
        educationResponse.setNameOfInstitution(education.getNameOfInstitution());
        educationResponse.setDegree(education.getDegree());
        educationResponse.setStartMonth(education.getStartMonth());
        educationResponse.setStartYear(education.getStartYear());
        educationResponse.setGraduationMonth(education.getGraduationMonth());
        educationResponse.setGraduationYear(education.getGraduationYear());
        return educationResponse;
    }


        public User findByEmail(String email) {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new UsernameNotFoundException("User not found with email: " + email);
            }
            return user;
        }





    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User updateDetails(long id,User userUpdate){
        Optional<User> existingUser = userRepository.findById(id);

        if(existingUser.isPresent()){
            User existingUser1=existingUser.get();
            existingUser1.setFirstName(userUpdate.getFirstName());
            existingUser1.setLastName(userUpdate.getLastName());
            existingUser1.setEmail(userUpdate.getEmail());
//            existingUser1.setPassword(userUpdate.getPassword());
            existingUser1.setBio(userUpdate.getBio());
            existingUser1.setDateOfBirth(userUpdate.getDateOfBirth());
            existingUser1.setGender(userUpdate.getGender());
            existingUser1.setLocation(userUpdate.getLocation());
            existingUser1.setPhoneNumber(userUpdate.getPhoneNumber());
            existingUser1.setWebsite(userUpdate.getWebsite());
            return userRepository.save(existingUser1);
        }else{
            throw new RuntimeException("id not found");
        }

    }

    @Override
    public User getUserById(Long userId) {
        return null;
    }

    public String updatePassword(String email,PasswordChange passwordChange){
        Optional<User> user= Optional.ofNullable(userRepository.findByEmail(email));
        if(user.isPresent()){
            User user1=user.get();
            if(passwordChange.getNewPassword().equals(passwordChange.getConfirmPassword())){
                if(passwordEncoder.matches(passwordChange.getOldPassword(), user1.getPassword())){
                    user1.setPassword(passwordEncoder.encode(passwordChange.getNewPassword()));
                    userRepository.save(user1);
                    return "password updated successfully";
                }else{
                    return "password not matches with the old password";
                }

            }else{
                return "new Password and confirm Password are not equal";
            }

        }else{
            return "user is not found";
        }


    }
    //saving profile photo and cover photo in database
    @Override
    public String saveProfilePhoto(String email, MultipartFile file) throws IOException {
        return saveFile(email, file, "profile");
    }

    @Override
    public String saveCoverPhoto(String email, MultipartFile file)  throws IOException {
        return saveFile(email, file, "cover");
    }

    @Override
    public String saveFile(String email, MultipartFile file, String type)throws IOException {
        User user = userRepository.findByEmail(email);

        byte[] fileContent = file.getBytes();
        if ("profile".equals(type)) {
            user.setProfilePhoto(fileContent);
        } else if ("cover".equals(type)) {
            user.setCoverPhoto(fileContent);
        }

        userRepository.save(user);
        return "File uploaded successfully: " + file.getOriginalFilename();
    }

    @Override
    public byte[] getProfilePhoto(String email) {
        User user = userRepository.findByEmail(email);
        return user != null ? user.getProfilePhoto() : null;
    }

    @Override
    public byte[] getCoverPhoto(String email) {
        User user = userRepository.findByEmail(email);;
        return user != null ? user.getCoverPhoto() : null;
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

}
