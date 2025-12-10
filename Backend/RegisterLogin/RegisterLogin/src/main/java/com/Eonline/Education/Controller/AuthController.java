package com.Eonline.Education.Controller;

import com.Eonline.Education.Configuration.JwtTokenProvider;
import com.Eonline.Education.Request.LoginRequest;
import com.Eonline.Education.Service.CustomUserDetails;
import com.Eonline.Education.Service.EmailService;
import com.Eonline.Education.Service.OtpService;
import com.Eonline.Education.exceptions.UserException;
import com.Eonline.Education.modals.OtpVerificationRequest;
import com.Eonline.Education.modals.User;
import com.Eonline.Education.modals.UserRegistrationRequest;
import com.Eonline.Education.repository.UserRepository;
import com.Eonline.Education.response.AuthResponse;
import jakarta.mail.MessagingException;
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

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    private CustomUserDetails customUserDetails;

    //    private CartService cartService;
    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpService otpService;


    private String generatedOtp;
    private String email;
    private String registeredFirstName;
    private String registeredLastName;

    private String registeredPassword;

    private String registeredRole;


    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, CustomUserDetails customUserDetails) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetails = customUserDetails;
//        this.cartService = cartService;

    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationRequest request, User user) throws MessagingException, jakarta.mail.MessagingException, UserException {
        // Check if the email is already registered
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already registered.");
        }

        // Generate OTP
        generatedOtp = otpService.generateOtp();

        // Send OTP via email
        emailService.sendOtpEmail(request.getEmail(), generatedOtp);

        // Store registered email for OTP verification
        email = request.getEmail();

        registeredFirstName = request.getFirstName();
        registeredLastName = request.getLastName();
        registeredPassword = request.getPassword();
        registeredRole = request.getRole();


        return ResponseEntity.ok("OTP sent successfully.");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody OtpVerificationRequest request) throws UserException {
        if (generatedOtp != null && request.getOtp().equals(generatedOtp)) {
            User created = new User();
            created.setFirstName(registeredFirstName);
            created.setLastName(registeredLastName);
            created.setEmail(email);
            created.setRole(registeredRole);
            created.setPassword(passwordEncoder.encode(registeredPassword));
            User savedUser = userRepository.save(created);

//                cartService.createCart(savedUser);
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, registeredPassword);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenProvider.generateToken(authentication);
            AuthResponse authResponse = new AuthResponse(token, true);
            // Clear stored OTP and registered email after successful registration
            generatedOtp = null;
            email = null;
            registeredFirstName = null;
            registeredLastName = null;
            return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);

        } else {
            // OTP verification failed
            return ResponseEntity.badRequest().body(new AuthResponse(null, false, "Invalid OTP. Registration failed."));
        }
    }


    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        System.out.println(username + " ----- " + password);

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);


        String token = jwtTokenProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();

        authResponse.setStatus(true);
        authResponse.setJwt(token);


        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserDetails.loadUserByUsername(username);

        System.out.println("sign in userDetails - " + userDetails);

        if (userDetails == null) {
            System.out.println("sign in userDetails - null " + userDetails);
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            System.out.println("sign in userDetails - password not match " + userDetails);
            throw new BadCredentialsException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
    @PostMapping("/forget")
    public ResponseEntity<String> forgetPassword(@RequestBody User user) throws MessagingException, jakarta.mail.MessagingException, UserException {
        // Check if the email is already registered
        User email = userRepository.findByEmail(user.getEmail());
        if (email != null) {
            // Generate OTP
            generatedOtp = otpService.generateOtp();

            // Send OTP via email
            emailService.sendOtpEmail(user.getEmail(), generatedOtp);


            return ResponseEntity.ok("OTP sent successfully.");

        } else {
            return ResponseEntity.badRequest().body("You entered invalid email.");
        }
    }

    @PostMapping("/validating-otp")
    public ResponseEntity<String> validatingOtp(@RequestBody OtpVerificationRequest request) throws UserException {
        if (generatedOtp != null && request.getOtp().equals(generatedOtp)) {


            return new ResponseEntity<String>("otp verified.", HttpStatus.OK);

        } else {
            // OTP verification failed
            return new ResponseEntity<String>("Invalid otp.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/confirmpwd/{email}")
    public ResponseEntity<String> confirmPassword(@PathVariable String email, @RequestBody User user) {
        String password = user.getPassword();
        System.out.println(password);
        String confirmPassword = user.getConfirmPassword();
        System.out.println(confirmPassword);

        if (!password.equals(confirmPassword)) {
            return new ResponseEntity<>("Passwords do not match", HttpStatus.BAD_REQUEST);
        }
        // Find the existing user by email
        User existingUser = userRepository.findByEmail(email);
        if (existingUser == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        // Update password
        existingUser.setPassword(passwordEncoder.encode(password));
        existingUser.setConfirmPassword(passwordEncoder.encode(confirmPassword));
        userRepository.save(existingUser);

        return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
    }
}





