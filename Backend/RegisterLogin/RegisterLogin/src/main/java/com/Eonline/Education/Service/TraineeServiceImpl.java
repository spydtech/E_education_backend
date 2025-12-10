package com.Eonline.Education.Service;

import com.Eonline.Education.Configuration.JwtTokenProvider;
import com.Eonline.Education.exceptions.UserException;
import com.Eonline.Education.modals.TraineeCredentialGenerator;
import com.Eonline.Education.repository.TraineeRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraineeServiceImpl implements TraineeService {

    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TraineeActivityService traineeActivityService;
    @Autowired
    private final JwtTokenProvider jwtTokenProvider;

    public  TraineeServiceImpl(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider=jwtTokenProvider;
    }

    @Override
    public String createUserId(TraineeCredentialGenerator traineeCredentialGenerator) throws MessagingException {
        if (traineeRepository.existsByEmail(traineeCredentialGenerator.getEmail())) {
            return "Email is already registered.";
        }

        String password = traineeCredentialGenerator.getPassword();
        traineeCredentialGenerator.setPassword(passwordEncoder.encode(password));

        traineeRepository.save(traineeCredentialGenerator);
        emailService.sendUserIdAndPassword(traineeCredentialGenerator.getEmail(), traineeCredentialGenerator.getUserId(), password);

        return "UserName and password are sent successfully";
    }

    public void logInTrainee(TraineeCredentialGenerator trainee) {
        traineeActivityService.traineeLoggedIn(trainee);
    }

    public void logOutTrainee(String userId) {
        traineeActivityService.traineeLoggedOut(userId);
    }
    @Override
    public TraineeCredentialGenerator findUserProfileByJwt(String jwt) throws UserException {
        String userId = jwtTokenProvider.getUserIdFromJwtToken(jwt);
        TraineeCredentialGenerator trainee = traineeRepository.findByUserId(userId);
        if (trainee == null) {
            throw new UserException("User not exist with email " + userId);
        }
        return trainee;
    }

    public List<TraineeCredentialGenerator> getAllTrainees(){
        return traineeRepository.findAll();
    }
}


//package com.Eonline.Education.Service;
//
//import com.Eonline.Education.Configuration.JwtTokenProvider;
//import com.Eonline.Education.Request.LoginRequest;
//import com.Eonline.Education.modals.TraineeCredentialGenerator;
//import com.Eonline.Education.repository.TraineeRepository;
//import com.Eonline.Education.response.AuthResponse;
//import jakarta.mail.MessagingException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.Random;
//
//@Service
//public class TraineeServiceImpl implements TraineeService {
//
//    @Autowired
//    TraineeRepository traineeCredentialRepository;
//    @Autowired
//    BCryptPasswordEncoder passwordEncoder;
//    @Autowired
//    private EmailService emailService;
//
//    public String creatingUserName(TraineeCredentialGenerator traineeCredentialGenerator) throws MessagingException {
//        if (traineeCredentialRepository.existsByEmail(traineeCredentialGenerator.getEmail())) {
//            return "Email is already registered.";
//        }
//
//        emailService.sendUserNameAndPassword(traineeCredentialGenerator.getEmail(), traineeCredentialGenerator.getUserName(), traineeCredentialGenerator.getPassword());
//        traineeCredentialGenerator.setPassword(passwordEncoder.encode(traineeCredentialGenerator.getPassword()));
//        traineeCredentialRepository.save(traineeCredentialGenerator);
//
//        return "UserName and password is sent successfully";
//    }
//
//    @Override
//    public String createUserName(TraineeCredentialGenerator traineeCredentialGenerator) throws MessagingException {
//        return "";
//    }
//}
