package com.Eonline.Education.Service;

import com.Eonline.Education.Configuration.JwtTokenProvider;
import com.Eonline.Education.exceptions.UserException;
import com.Eonline.Education.modals.*;
import com.Eonline.Education.repository.AccountRepository;
import com.Eonline.Education.repository.EducationRepository;
import com.Eonline.Education.repository.UserRepository;
import com.Eonline.Education.response.*;
import com.Eonline.Education.user.UserStatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AccountRepository accountRepository;
    private final EducationRepository educationRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository,
                                     JwtTokenProvider jwtTokenProvider,
                                     AccountRepository accountRepository,
                                     EducationRepository educationRepository,
                                     PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.accountRepository = accountRepository;
        this.educationRepository = educationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findUserById(Long userId) throws UserException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User not found with id " + userId));
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        // ✅ FIXED: Handle Optional properly
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("User not exist with email " + email));
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public void deleteUser(Long userId) {
        // Implementation for delete user
        userRepository.deleteById(userId);
    }

    @Override
    public ResponseEntity<?> getAccountDetails(String email) {
        // ✅ FIXED: Handle Optional properly
        User userDetails = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        Long userAccID = userDetails.getId();

        if (!accountRepository.existsByUserId(userAccID)) {
            Account accountDetails = new Account();
            accountDetails.setUser(userDetails);
            accountDetails.setUserEmail(userDetails.getEmail());
            accountDetails.setFullName(userDetails.getFirstName() + " " + userDetails.getLastName());
            accountRepository.save(accountDetails);
            return ResponseEntity.ok(accountDetails);
        } else {
            Account account = accountRepository.findByUserId(userAccID);
            AccountResponse accountResponse = new AccountResponse();
            accountResponse.setFullName(account.getFullName());
            accountResponse.setUserEmail(account.getUserEmail());
            accountResponse.setLocation(account.getLocation());
            accountResponse.setPhoneNumber(account.getPhoneNumber());
            return ResponseEntity.ok(accountResponse);
        }
    }

    @Override
    public ResponseEntity<?> updateAccountDetails(String email, Account userAccount) {
        // ✅ FIXED: Handle Optional properly
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

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
        // ✅ FIXED: Handle Optional properly
        User currentEduUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

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
        // ✅ FIXED: Handle Optional properly
        User userEducationDetails = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

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

    @Override
    public User findByEmail(String email) {
        // ✅ FIXED: Handle Optional properly
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateDetails(long id, User userUpdate) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setFirstName(userUpdate.getFirstName());
                    existingUser.setLastName(userUpdate.getLastName());
                    existingUser.setEmail(userUpdate.getEmail());
                    existingUser.setBio(userUpdate.getBio());
                    existingUser.setDateOfBirth(userUpdate.getDateOfBirth());
                    existingUser.setGender(userUpdate.getGender());
                    existingUser.setLocation(userUpdate.getLocation());
                    existingUser.setPhoneNumber(userUpdate.getPhoneNumber());
                    existingUser.setWebsite(userUpdate.getWebsite());
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    @Override
    public String updatePassword(String email, PasswordChange passwordChange) {
        // ✅ FIXED: Handle Optional properly
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (passwordChange.getNewPassword().equals(passwordChange.getConfirmPassword())) {
            if (passwordEncoder.matches(passwordChange.getOldPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(passwordChange.getNewPassword()));
                userRepository.save(user);
                return "Password updated successfully";
            } else {
                return "New password does not match with the old password";
            }
        } else {
            return "New password and confirm password do not match";
        }
    }

    @Override
    public String saveProfilePhoto(String email, MultipartFile file) throws IOException {
        return saveFile(email, file, "profile");
    }

    @Override
    public String saveCoverPhoto(String email, MultipartFile file) throws IOException {
        return saveFile(email, file, "cover");
    }

    @Override
    public String saveFile(String email, MultipartFile file, String type) throws IOException {
        // ✅ FIXED: Handle Optional properly
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

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
        // ✅ FIXED: Handle Optional properly
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return user.getProfilePhoto();
    }

    @Override
    public byte[] getCoverPhoto(String email) {
        // ✅ FIXED: Handle Optional properly
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return user.getCoverPhoto();
    }

    @Override
    public ResponseEntity<Account> accountSave(String jwt, Account account) {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        // ✅ FIXED: Handle Optional properly
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        Account account1 = new Account();
        account1.setPhoneNumber(account.getPhoneNumber());
        account1.setUserEmail(account.getUserEmail());
        account1.setLocation(account.getLocation());
        account1.setFullName(account.getFullName());
        account1.setUser(user);
        accountRepository.save(account1);
        return ResponseEntity.ok(account1);
    }

    @Override
    public ResponseEntity<AdminProfileResponse> adminProfileUpdate(Long id, MultipartFile file,
                                                                   String firstName, String lastName,
                                                                   String email, String phoneNumber) throws IOException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setProfilePhoto(file.getBytes());
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        userRepository.save(user);

        return ResponseEntity.ok(userToAdminDetails(user, file));
    }

    @Override
    public UserProfileResponse findUserProfile(String jwt) {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        // ✅ FIXED: Handle Optional properly
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return userToProfileResponse(user);
    }

    private UserProfileResponse userToProfileResponse(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getProfilePhoto(),
                user.getStatus()
        );
    }

    private AdminProfileResponse userToAdminDetails(User user, MultipartFile file) {
        AdminProfileResponse ad = new AdminProfileResponse();
        ad.setEmail(user.getEmail());
        ad.setPhoneNumber(user.getPhoneNumber());
        ad.setProfilePhoto(file.getOriginalFilename());
        ad.setFirstName(user.getFirstName());
        ad.setLastName(user.getLastName());
        return ad;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Map<String, Long> activeInactiveCount() {
        Map<String, Long> map = new HashMap<>();
        long activeUserCount = userRepository.findAllByStatus(UserStatus.ACTIVE).size();
        long inActiveUserCount = userRepository.findAllByStatus(UserStatus.INACTIVE).size();
        map.put("activeUserCount", activeUserCount);
        map.put("inactiveUserCount", inActiveUserCount);
        return map;
    }
}