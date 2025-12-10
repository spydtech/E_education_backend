package com.Eonline.Education.Controller;

import com.Eonline.Education.Configuration.JwtTokenProvider;
import com.Eonline.Education.Service.CustomerTraineeDetails;
import com.Eonline.Education.Service.TraineeActivityService;
import com.Eonline.Education.Service.TraineeService;
import com.Eonline.Education.modals.TraineeCredentialGenerator;
import com.Eonline.Education.response.AuthResponse;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/trainee")
public class TraineeController {

    private static final Logger logger = LoggerFactory.getLogger(TraineeController.class);

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerTraineeDetails customerTraineeDetails;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private TraineeActivityService traineeActivityService;

    // Endpoint for registering new trainees
    @PostMapping("/register")
    public ResponseEntity<?> createLoginCredentials(@RequestBody TraineeCredentialGenerator traineeCredentialGenerator) {
        try {
            String response = traineeService.createUserId(traineeCredentialGenerator);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (MessagingException e) {
            logger.error("Error sending email for user registration: {}", e.getMessage());
            return new ResponseEntity<>("Error sending email for user registration.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("Registration error: {}", e.getMessage());
            return new ResponseEntity<>("An error occurred during registration.", HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint for trainee signin (authentication)
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody TraineeCredentialGenerator traineeCredentialGenerator) {
        try {
            // Authenticate trainee
            Authentication authentication = authenticate(traineeCredentialGenerator.getUserId(), traineeCredentialGenerator.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String token = jwtTokenProvider.generateToken(authentication);
            AuthResponse authResponse = new AuthResponse(token, true);

            // Log trainee login event
            traineeService.logInTrainee(traineeCredentialGenerator);

            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            logger.error("Authentication error: {}", e.getMessage());
            return new ResponseEntity<>(new AuthResponse("Invalid userId or password", false), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            logger.error("Signin error: {}", e.getMessage());
            return new ResponseEntity<>(new AuthResponse("An error occurred during signin", false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<TraineeCredentialGenerator> getUserProfileHandler(@RequestHeader("Authorization") String jwt) throws Exception {
        TraineeCredentialGenerator trainee = traineeService.findUserProfileByJwt(jwt);
        return new ResponseEntity<>(trainee, HttpStatus.ACCEPTED);
    }

    // Endpoint for trainee logout
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String userId) {
        traineeService.logOutTrainee(userId);
        return new ResponseEntity<>("Trainee logged out successfully", HttpStatus.OK);
    }

    // Endpoint to get active trainees
    @GetMapping("/active-trainees")
    public ResponseEntity<Map<String, TraineeCredentialGenerator>> getActiveTrainees() {
        return new ResponseEntity<>(traineeActivityService.getActiveTrainees(), HttpStatus.OK);
    }

    // Endpoint to get count of active trainees
    @GetMapping("/active-trainees-count")
    public ResponseEntity<Integer> getActiveTraineeCount() {
        return new ResponseEntity<>(traineeActivityService.getActiveTraineeCount(), HttpStatus.OK);
    }

    // Endpoint to get count of inactive trainees
    @GetMapping("/inactive-trainees-count")
    public ResponseEntity<Integer> getInactiveTraineeCount() {
        return new ResponseEntity<>(traineeActivityService.getInactiveTraineeCount(), HttpStatus.OK);
    }

    // Helper method to authenticate trainee
    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customerTraineeDetails.loadUserByUsername(username);

        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid userId or password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @GetMapping("/getAllTrainee")
    public ResponseEntity<List<TraineeCredentialGenerator>> getAllTrainess(){
        return new ResponseEntity<>(traineeService.getAllTrainees(),HttpStatus.OK);
    }
}
