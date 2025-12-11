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

    @PutMapping("update/trainee/{email}")
    public ResponseEntity<TraineeCredentialGenerator> update(@RequestBody TraineeCredentialGenerator traineeCredentialGenerator){
        return ResponseEntity.ok(traineeService.update(traineeCredentialGenerator));
    }

    // Endpoint for trainee signin (authentication)
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody TraineeCredentialGenerator traineeCredentialGenerator) {
        try {
            String trainee = traineeCredentialGenerator.getEmail() != null
                    ? traineeCredentialGenerator.getEmail()
                    : traineeCredentialGenerator.getUserId();
            Authentication authentication = traineeCredentialGenerator.getEmail() != null
                    ? authenticateEmial(trainee, traineeCredentialGenerator.getPassword())
                    : authenticate(trainee, traineeCredentialGenerator.getPassword());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenProvider.generateToken(authentication);

            AuthResponse authResponse = new AuthResponse(token, true);
            if (traineeCredentialGenerator.getEmail() != null) {
                traineeService.logInTraineeEmail(traineeCredentialGenerator);
            } else {
                traineeService.logInTrainee(traineeCredentialGenerator);
            }

            return ResponseEntity.ok(authResponse);
        } catch (BadCredentialsException e) {
            logger.error("Authentication error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("Invalid userId or password", false));
        } catch (Exception e) {
            logger.error("Signin error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse("An error occurred during signin", false));
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
    private Authentication authenticateEmial(String email, String password) {
        UserDetails userDetails = customerTraineeDetails.loadUserByEmail(email);

        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid Email or password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @GetMapping("/getAllTrainee")
    public ResponseEntity<List<TraineeCredentialGenerator>> getAllTrainess(){
        return new ResponseEntity<>(traineeService.getAllTrainees(),HttpStatus.OK);
    }
    @DeleteMapping("/delete/{email}")
    public String delete(@PathVariable String email){
        return traineeService.delete(email);

    }
//admin side dashboard
    @GetMapping("/count")
    public Map<String,Long> countOfTrainers(@RequestHeader("Authorization") String jwt){
     return traineeService.countOfTrainers(jwt);
    }

}
