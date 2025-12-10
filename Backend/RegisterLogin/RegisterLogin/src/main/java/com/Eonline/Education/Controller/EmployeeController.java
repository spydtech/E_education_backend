package com.Eonline.Education.Controller;

import com.Eonline.Education.Configuration.JwtTokenProvider;
import com.Eonline.Education.Request.EmployeeLoginRequest;
import com.Eonline.Education.Request.LoginRequest;
import com.Eonline.Education.Service.*;
import com.Eonline.Education.exceptions.UserException;
import com.Eonline.Education.modals.*;
import com.Eonline.Education.repository.EmployeeRepository;
import com.Eonline.Education.response.AuthResponse;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/employee")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private CustomEmployeeDetails customEmployeeDetails;

    @Autowired
    private EmployeeService employeeService;

    private String generatedOtp;
    private String EmployeeEmail;
    private String registeredName;
    private String registeredPassword;

    @PostMapping("/register")
    //@Secured("ADMIN")
    public ResponseEntity<String> registerUser(@RequestBody EmployeeRegistrationRequest request, Employee employee) throws MessagingException, jakarta.mail.MessagingException, UserException {
        // Check if the email is already registered
        if (employeeRepository.existsByEmployeeEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already registered.");
        }

        // Generate OTP
        generatedOtp = otpService.generateOtp();

        // Send OTP via email
        emailService.sendOtpEmail(request.getEmail(), generatedOtp);

        // Store registered email for OTP verification
        EmployeeEmail = request.getEmail();
        registeredName = request.getName();
        registeredPassword = request.getPassword();

        return ResponseEntity.ok("OTP sent successfully.");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody OtpVerificationRequest request) throws UserException, MessagingException {
        if(generatedOtp !=null && request.getOtp().equals(generatedOtp)){
            Employee created = new Employee();
            created.setEmployeeId(employeeService.generateUniqueUserId());
            created.setEmployeeName(registeredName);
            created.setEmployeeEmail(EmployeeEmail);
            created.setEmployeePassword(passwordEncoder.encode(registeredPassword));

            Employee savedEmployee=employeeRepository.save(created);

            Authentication authentication = new UsernamePasswordAuthenticationToken(EmployeeEmail, registeredPassword);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenProvider.generateToken(authentication);
            AuthResponse authResponse= new AuthResponse(token,true);

            Employee currentEmployee = employeeRepository.findByEmployeeEmail(EmployeeEmail);
            emailService.sendEmployeeID(currentEmployee.getEmployeeEmail(), currentEmployee.getEmployeeId());

            // Clear stored OTP and registered email after successful registration
            generatedOtp = null;
            EmployeeEmail = null;
            registeredName = null;



            return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);

        }else {
            // OTP verification failed
            return ResponseEntity.badRequest().body(new AuthResponse(null, false, "Invalid OTP. Registration failed."));
        }
    }
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody EmployeeLoginRequest employeeLoginRequest) {
        String username = employeeLoginRequest.getEmployeeId();
        String password = employeeLoginRequest.getEmployeePassword();

        System.out.println(username +" ----- "+password);

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);


        String token = jwtTokenProvider.generateToken(authentication);
        AuthResponse authResponse= new AuthResponse();

        authResponse.setStatus(true);
        authResponse.setJwt(token);



        return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customEmployeeDetails.loadUserByUsername(username);


        System.out.println("sign in userDetails - "+userDetails);

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

}
