//package com.Eonline.Education.Controller;
//
//import com.Eonline.Education.Configuration.JwtTokenProvider;
//import com.Eonline.Education.Request.LoginRequest;
//import com.Eonline.Education.Request.GoogleAuthRequest;
//import com.Eonline.Education.Service.CustomUserDetails;
//import com.Eonline.Education.Service.EmailService;
//import com.Eonline.Education.Service.NotificationService;
//import com.Eonline.Education.Service.OtpService;
//import com.Eonline.Education.exceptions.AuthenticationBasedException;
//import com.Eonline.Education.modals.OtpVerificationRequest;
//import com.Eonline.Education.modals.User;
//import com.Eonline.Education.modals.UserRegistrationRequest;
//import com.Eonline.Education.repository.UserRepository;
//import com.Eonline.Education.response.AuthResponse;
//import com.Eonline.Education.user.UserRole;
//import com.Eonline.Education.user.UserStatus;
//import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
//import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.gson.GsonFactory;
//import jakarta.mail.MessagingException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AbstractUserDetailsReactiveAuthenticationManager;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//import java.security.GeneralSecurityException;
//import java.util.Collections;
//import java.util.Optional;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;
//
//    @Autowired
//    private CustomUserDetails customUserDetails;
//
//    @Autowired
//    private EmailService emailService;
//
//    @Autowired
//    private OtpService otpService;
//
//    @Autowired
//    private NotificationService notificationService;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    private String generatedOtp;
//    private String email;
//    private String registeredFirstName;
//    private String registeredLastName;
//    private String registeredPassword;
//    private UserRole registeredRole;
//
//    @Value("${google.client-id}")
//    private String googleClientId;
//
//    // ========== Registration ==========
//    @PostMapping("/register")
//    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationRequest request)
//            throws MessagingException, AuthenticationBasedException {
//
//        if (userRepository.existsByEmail(request.getEmail())) {
//            return ResponseEntity.badRequest().body("Email is already registered.");
//        }
//
//        generatedOtp = otpService.generateOtp();
//        emailService.sendOtpEmail(request.getEmail(), generatedOtp);
//
//        email = request.getEmail();
//        registeredFirstName = request.getFirstName();
//        registeredLastName = request.getLastName();
//        registeredPassword = request.getPassword();
//        registeredRole = UserRole.CUSTOMER;
//
//        return ResponseEntity.ok("OTP sent successfully.");
//    }
//
//    @PostMapping("/verify-otp")
//    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody OtpVerificationRequest request)
//            throws AuthenticationBasedException {
//
//        if (generatedOtp != null && request.getOtp().equals(generatedOtp)) {
//            User created = new User();
//            created.setFirstName(registeredFirstName);
//            created.setLastName(registeredLastName);
//            created.setEmail(email);
//            created.setRole(String.valueOf(registeredRole));
//            created.setStatus(UserStatus.INACTIVE);
//            created.setPassword(passwordEncoder.encode(registeredPassword));
//
//            User savedUser = userRepository.save(created);
//
//            Authentication authentication = new UsernamePasswordAuthenticationToken(email, registeredPassword);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            String token = jwtTokenProvider.generateToken(authentication);
//            AuthResponse authResponse = new AuthResponse(token, true);
//
//            // Clear registration data
//            generatedOtp = null;
//            email = null;
//            registeredFirstName = null;
//            registeredLastName = null;
//            registeredPassword = null;
//            registeredRole = null;
//
//            return new ResponseEntity<>(authResponse, HttpStatus.OK);
//        } else {
//            return ResponseEntity.badRequest()
//                    .body(new AuthResponse(null, false, "Invalid OTP. Registration failed."));
//        }
//    }
//
//    // ========== Login ==========
//    // ========== Login ==========
//    @PostMapping("/signin")
//    public ResponseEntity<?> signin(@RequestBody LoginRequest loginRequest) {
//        try {
//            String username = loginRequest.getEmail();
//            String password = loginRequest.getPassword();
//
//            System.out.println("=== LOGIN DEBUG ===");
//            System.out.println("Login attempt for: " + username);
//
//            // Check if user exists first
//            Optional<User> userOptional = userRepository.findByEmail(username);
//            if (userOptional.isEmpty()) {
//                System.out.println("User not found: " + username);
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("User not found with email: " + username);
//            }
//
//            User user = userOptional.get();
//            System.out.println("User found: " + user.getEmail());
//            System.out.println("User role: " + user.getRole());
//            System.out.println("User status: " + user.getStatus());
//
//            // Debug password
//            boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());
//            System.out.println("Password matches: " + passwordMatches);
//
//            if (!passwordMatches) {
//                System.out.println("Password mismatch for: " + username);
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                        .body("Invalid username or password");
//            }
//
//            // Use AuthenticationManager for authentication
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(username, password)
//            );
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            // Update user status to ACTIVE
//            user.setStatus(UserStatus.ACTIVE);
//            userRepository.save(user);
//
//            // Generate JWT token
//            String token = jwtTokenProvider.generateToken(authentication);
//            System.out.println("Token generated successfully");
//
//            // Create response
//            AuthResponse authResponse = new AuthResponse(token, true);
//            authResponse.setRole(user.getRole());
//            authResponse.setEmail(user.getEmail());
//            authResponse.setFullName(user.getFirstName() + " " + user.getLastName());
//
//            return new ResponseEntity<>(authResponse, HttpStatus.OK);
//
//        } catch (BadCredentialsException e) {
//            System.err.println("Bad credentials for: " + loginRequest.getEmail());
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("Invalid username or password");
//        } catch (Exception e) {
//            System.err.println("Login error: " + e.getMessage());
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("An error occurred during login: " + e.getMessage());
//        }
//    }
//
//    private Authentication authenticate(String username, String password) {
//        UserDetails userDetails = customUserDetails.loadUserByUsername(username);
//        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
//            throw new BadCredentialsException("Invalid username or password");
//        }
//        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//    }
//
//
//
//    // ========== Logout ==========
//    @PostMapping("/logout/{email}")
//    public ResponseEntity<String> logoutUser(@PathVariable String email) {
//        try {
//            // FIXED: Use orElseThrow to get User from Optional
//            User user = userRepository.findByEmail(email)
//                    .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
//
//            user.setStatus(UserStatus.INACTIVE);
//            userRepository.save(user);
//
//            SecurityContextHolder.clearContext();
//
//            return ResponseEntity.ok("User logged out successfully.");
//
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("User not found with email: " + email);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("An error occurred during logout: " + e.getMessage());
//        }
//    }
//
//    // ========== Forgot Password ==========
//    @PostMapping("/forget")
//    public ResponseEntity<String> forgetPassword(@RequestBody User userRequest)
//            throws MessagingException, AuthenticationBasedException {
//
//        try {
//            // FIXED: Use Optional properly with isPresent() check
//            Optional<User> userOpt = userRepository.findByEmail(userRequest.getEmail());
//
//            if (userOpt.isPresent()) {
//                generatedOtp = otpService.generateOtp();
//                emailService.sendOtpEmail(userRequest.getEmail(), generatedOtp);
//                return ResponseEntity.ok("OTP sent successfully to " + userRequest.getEmail());
//            } else {
//                return ResponseEntity.badRequest()
//                        .body("No account found with email: " + userRequest.getEmail());
//            }
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to process forget password request: " + e.getMessage());
//        }
//    }
//
//    @PostMapping("/validating-otp")
//    public ResponseEntity<String> validatingOtp(@RequestBody OtpVerificationRequest request) {
//        try {
//            if (generatedOtp != null && request.getOtp().equals(generatedOtp)) {
//                return ResponseEntity.ok("OTP verified successfully.");
//            } else {
//                return ResponseEntity.badRequest()
//                        .body("Invalid OTP. Please try again.");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error validating OTP: " + e.getMessage());
//        }
//    }
//
//    @PostMapping("/confirmpwd/{email}")
//    public ResponseEntity<String> confirmPassword(@PathVariable String email, @RequestBody User userRequest) {
//        try {
//            String password = userRequest.getPassword();
//            String confirmPassword = userRequest.getConfirmPassword();
//
//            // Validate passwords
//            if (password == null || password.trim().isEmpty()) {
//                return ResponseEntity.badRequest()
//                        .body("Password cannot be empty");
//            }
//
//            if (!password.equals(confirmPassword)) {
//                return ResponseEntity.badRequest()
//                        .body("Passwords do not match");
//            }
//
//            // Find user - FIXED: Use orElseThrow to get User from Optional
//            User existingUser = userRepository.findByEmail(email)
//                    .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
//
//            // Update password
//            existingUser.setPassword(passwordEncoder.encode(password));
//            // Note: confirmPassword is transient, don't save it to database
//
//            userRepository.save(existingUser);
//
//            // Send notification
//            notificationService.createNotification(existingUser.getEmail(), "Password updated successfully");
//
//            return ResponseEntity.ok("Password updated successfully");
//
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("User not found with email: " + email);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to update password: " + e.getMessage());
//        }
//    }
//
//    // ========== Google Login ==========
//    @PostMapping("/google")
//    public ResponseEntity<?> googleAuth(@RequestBody GoogleAuthRequest googleAuthRequest) {
//        try {
//            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
//                    new NetHttpTransport(),
//                    new GsonFactory()
//            )
//                    .setAudience(Collections.singletonList(googleClientId))
//                    .build();
//
//            GoogleIdToken idToken = verifier.verify(googleAuthRequest.getToken());
//
//            if (idToken != null) {
//                GoogleIdToken.Payload payload = idToken.getPayload();
//
//                String email = payload.getEmail();
//                String name = (String) payload.get("name");
//                String pictureUrl = (String) payload.get("picture");
//                String givenName = (String) payload.get("given_name");
//                String familyName = (String) payload.get("family_name");
//
//                // Check if user exists - FIXED: Use Optional properly
//                Optional<User> userOpt = userRepository.findByEmail(email);
//                User user;
//
//                if (userOpt.isEmpty()) {
//                    // Create new user for Google login
//                    user = new User();
//                    user.setEmail(email);
//                    user.setFirstName(givenName != null ? givenName : name);
//                    user.setLastName(familyName != null ? familyName : "");
//                    user.setRole(String.valueOf(UserRole.CUSTOMER));
//                    user.setStatus(UserStatus.ACTIVE);
//                    user.setProfilePicture(pictureUrl);
//
//                    // Set a random password for Google users (won't be used for login)
//                    String randomPassword = passwordEncoder.encode(UUID.randomUUID().toString());
//                    user.setPassword(randomPassword);
//
//                    user = userRepository.save(user);
//                } else {
//                    user = userOpt.get();
//                    // Update user status to ACTIVE
//                    user.setStatus(UserStatus.ACTIVE);
//                    user = userRepository.save(user);
//                }
//
//                // Create authentication
//                Authentication auth = new UsernamePasswordAuthenticationToken(
//                        email,
//                        null,
//                        Collections.emptyList()
//                );
//
//                // Generate JWT token
//                String token = jwtTokenProvider.generateToken(auth);
//
//                // Create response
//                AuthResponse authResponse = new AuthResponse(token, true);
//                authResponse.setRole(user.getRole());
//                authResponse.setEmail(user.getEmail());
//                authResponse.setFullName(user.getFirstName() + " " + user.getLastName());
//
//                return new ResponseEntity<>(authResponse, HttpStatus.OK);
//            } else {
//                return ResponseEntity.badRequest()
//                        .body("Invalid Google token");
//            }
//
//        } catch (GeneralSecurityException | IOException e) {
//            return ResponseEntity.badRequest()
//                    .body("Google authentication failed: " + e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("An error occurred during Google authentication: " + e.getMessage());
//        }
//    }
//
//    // ========== Check Email Availability ==========
//    @GetMapping("/check-email/{email}")
//    public ResponseEntity<Boolean> checkEmailAvailability(@PathVariable String email) {
//        boolean exists = userRepository.existsByEmail(email);
//        return ResponseEntity.ok(!exists); // Return true if email is available
//    }
//
//    // ========== Health Check ==========
//    @GetMapping("/health")
//    public ResponseEntity<String> healthCheck() {
//        return ResponseEntity.ok("Auth Service is running");
//    }
//}








package com.Eonline.Education.Controller;

import com.Eonline.Education.Configuration.JwtTokenProvider;
import com.Eonline.Education.Request.LoginRequest;
import com.Eonline.Education.Request.GoogleAuthRequest;
import com.Eonline.Education.Service.EmailService;
import com.Eonline.Education.Service.NotificationService;
import com.Eonline.Education.Service.OtpService;
import com.Eonline.Education.exceptions.AuthenticationBasedException;
import com.Eonline.Education.modals.OtpVerificationRequest;
import com.Eonline.Education.modals.User;
import com.Eonline.Education.modals.UserRegistrationRequest;
import com.Eonline.Education.repository.UserRepository;
import com.Eonline.Education.response.AuthResponse;
import com.Eonline.Education.user.UserRole;
import com.Eonline.Education.user.UserStatus;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Temporary storage for registration (not thread-safe - consider using Redis for production)
    private String generatedOtp;
    private String email;
    private String registeredFirstName;
    private String registeredLastName;
    private String registeredPassword;
    private UserRole registeredRole;

    @Value("${google.client-id}")
    private String googleClientId;

    // ========== Registration ==========
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationRequest request)
            throws MessagingException, AuthenticationBasedException {

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already registered.");
        }

        generatedOtp = otpService.generateOtp();
        emailService.sendOtpEmail(request.getEmail(), generatedOtp);

        email = request.getEmail();
        registeredFirstName = request.getFirstName();
        registeredLastName = request.getLastName();
        registeredPassword = request.getPassword();
        registeredRole = UserRole.CUSTOMER;

        return ResponseEntity.ok("OTP sent successfully.");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody OtpVerificationRequest request)
            throws AuthenticationBasedException {

        if (generatedOtp != null && request.getOtp().equals(generatedOtp)) {
            User created = new User();
            created.setFirstName(registeredFirstName);
            created.setLastName(registeredLastName);
            created.setEmail(email);
            created.setRole(String.valueOf(registeredRole));
            created.setStatus(UserStatus.INACTIVE);
            created.setPassword(passwordEncoder.encode(registeredPassword));

            User savedUser = userRepository.save(created);

            Authentication authentication = new UsernamePasswordAuthenticationToken(email, registeredPassword);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenProvider.generateToken(authentication);
            AuthResponse authResponse = new AuthResponse(token, true);

            // Clear registration data
            generatedOtp = null;
            email = null;
            registeredFirstName = null;
            registeredLastName = null;
            registeredPassword = null;
            registeredRole = null;

            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest()
                    .body(new AuthResponse(null, false, "Invalid OTP. Registration failed."));
        }
    }

    // ========== Login ==========
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody LoginRequest loginRequest) {
        try {
            String username = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            System.out.println("=== LOGIN DEBUG ===");
            System.out.println("Login attempt for: " + username);

            // Check if user exists first
            Optional<User> userOptional = userRepository.findByEmail(username);
            if (userOptional.isEmpty()) {
                System.out.println("User not found: " + username);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found with email: " + username);
            }

            User user = userOptional.get();
            System.out.println("User found: " + user.getEmail());
            System.out.println("User role: " + user.getRole());
            System.out.println("User status: " + user.getStatus());

            // Debug password
            boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());
            System.out.println("Password matches: " + passwordMatches);

            if (!passwordMatches) {
                System.out.println("Password mismatch for: " + username);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid username or password");
            }

            // Use AuthenticationManager for authentication
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Update user status to ACTIVE
            user.setStatus(UserStatus.ACTIVE);
            userRepository.save(user);

            // Generate JWT token
            String token = jwtTokenProvider.generateToken(authentication);
            System.out.println("Token generated successfully");

            // Create response
            AuthResponse authResponse = new AuthResponse(token, true);
            authResponse.setRole(user.getRole());
            authResponse.setEmail(user.getEmail());
            authResponse.setFullName(user.getFirstName() + " " + user.getLastName());

            return new ResponseEntity<>(authResponse, HttpStatus.OK);

        } catch (BadCredentialsException e) {
            System.err.println("Bad credentials for: " + loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during login: " + e.getMessage());
        }
    }

    // ========== Logout ==========
    @PostMapping("/logout/{email}")
    public ResponseEntity<String> logoutUser(@PathVariable String email) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

            user.setStatus(UserStatus.INACTIVE);
            userRepository.save(user);

            SecurityContextHolder.clearContext();

            return ResponseEntity.ok("User logged out successfully.");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with email: " + email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during logout: " + e.getMessage());
        }
    }

    // ========== Forgot Password ==========
    @PostMapping("/forget")
    public ResponseEntity<String> forgetPassword(@RequestBody User userRequest)
            throws MessagingException, AuthenticationBasedException {

        try {
            Optional<User> userOpt = userRepository.findByEmail(userRequest.getEmail());

            if (userOpt.isPresent()) {
                generatedOtp = otpService.generateOtp();
                emailService.sendOtpEmail(userRequest.getEmail(), generatedOtp);
                return ResponseEntity.ok("OTP sent successfully to " + userRequest.getEmail());
            } else {
                return ResponseEntity.badRequest()
                        .body("No account found with email: " + userRequest.getEmail());
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process forget password request: " + e.getMessage());
        }
    }

    @PostMapping("/validating-otp")
    public ResponseEntity<String> validatingOtp(@RequestBody OtpVerificationRequest request) {
        try {
            if (generatedOtp != null && request.getOtp().equals(generatedOtp)) {
                return ResponseEntity.ok("OTP verified successfully.");
            } else {
                return ResponseEntity.badRequest()
                        .body("Invalid OTP. Please try again.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error validating OTP: " + e.getMessage());
        }
    }

    @PostMapping("/confirmpwd/{email}")
    public ResponseEntity<String> confirmPassword(@PathVariable String email, @RequestBody User userRequest) {
        try {
            String password = userRequest.getPassword();
            String confirmPassword = userRequest.getConfirmPassword();

            // Validate passwords
            if (password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Password cannot be empty");
            }

            if (!password.equals(confirmPassword)) {
                return ResponseEntity.badRequest()
                        .body("Passwords do not match");
            }

            // Find user
            User existingUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

            // Update password
            existingUser.setPassword(passwordEncoder.encode(password));
            userRepository.save(existingUser);

            // Send notification
            notificationService.createNotification(existingUser.getEmail(), "Password updated successfully");

            return ResponseEntity.ok("Password updated successfully");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with email: " + email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update password: " + e.getMessage());
        }
    }

    // ========== Google Login ==========
    @PostMapping("/google")
    public ResponseEntity<?> googleAuth(@RequestBody GoogleAuthRequest googleAuthRequest) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    new GsonFactory()
            )
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(googleAuthRequest.getToken());

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");
                String givenName = (String) payload.get("given_name");
                String familyName = (String) payload.get("family_name");

                // Check if user exists
                Optional<User> userOpt = userRepository.findByEmail(email);
                User user;

                if (userOpt.isEmpty()) {
                    // Create new user for Google login
                    user = new User();
                    user.setEmail(email);
                    user.setFirstName(givenName != null ? givenName : name);
                    user.setLastName(familyName != null ? familyName : "");
                    user.setRole(String.valueOf(UserRole.CUSTOMER));
                    user.setStatus(UserStatus.ACTIVE);
                    user.setProfilePicture(pictureUrl);

                    // Set a random password for Google users
                    String randomPassword = passwordEncoder.encode(UUID.randomUUID().toString());
                    user.setPassword(randomPassword);

                    user = userRepository.save(user);
                } else {
                    user = userOpt.get();
                    // Update user status to ACTIVE
                    user.setStatus(UserStatus.ACTIVE);
                    user = userRepository.save(user);
                }

                // Create authentication
                Authentication auth = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        Collections.emptyList()
                );

                // Generate JWT token
                String token = jwtTokenProvider.generateToken(auth);

                // Create response
                AuthResponse authResponse = new AuthResponse(token, true);
                authResponse.setRole(user.getRole());
                authResponse.setEmail(user.getEmail());
                authResponse.setFullName(user.getFirstName() + " " + user.getLastName());

                return new ResponseEntity<>(authResponse, HttpStatus.OK);
            } else {
                return ResponseEntity.badRequest()
                        .body("Invalid Google token");
            }

        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.badRequest()
                    .body("Google authentication failed: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during Google authentication: " + e.getMessage());
        }
    }

    // ========== Check Email Availability ==========
    @GetMapping("/check-email/{email}")
    public ResponseEntity<Boolean> checkEmailAvailability(@PathVariable String email) {
        boolean exists = userRepository.existsByEmail(email);
        return ResponseEntity.ok(!exists); // Return true if email is available
    }

    // ========== Health Check ==========
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Auth Service is running");
    }
}