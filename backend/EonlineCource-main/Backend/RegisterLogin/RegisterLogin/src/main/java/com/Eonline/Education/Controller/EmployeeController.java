package com.Eonline.Education.Controller;

import com.Eonline.Education.Configuration.JwtTokenProvider;
import com.Eonline.Education.Request.EmployeeLoginRequest;
import com.Eonline.Education.Service.*;
import com.Eonline.Education.exceptions.AuthenticationBasedException;
import com.Eonline.Education.modals.*;
import com.Eonline.Education.repository.EmployeeRepository;
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
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String password;
    private String email;


    @PostMapping("/register")
    //@Secured("ADMIN")
    public ResponseEntity<String> registerUser(@RequestBody EmployeeRegistrationRequest request, Employee employee) throws MessagingException, jakarta.mail.MessagingException, AuthenticationBasedException {
        // Check if the email is already registered
        if (employeeRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already registered.");
        }

//        // Generate OTP
//        generatedOtp = otpService.generateOtp();
//
//        // Send OTP via email
//        emailService.sendOtpEmail(request.getEmail(), generatedOtp);
//
//        // Store registered email for OTP verification
//        email= request.getEmail();
//        firstName = request.getFirstName();
//        lastName=request.getLastName();
//        password = request.getPassword();
//        phoneNumber=request.getPhoneNumber();
//
//        return ResponseEntity.ok("OTP sent successfully.");
        Employee created = new Employee();
        created.setEmployeeId(employeeService.generateUniqueUserId());
        created.setFirstName(request.getFirstName());
        created.setLastName(request.getLastName());
        created.setEmail(request.getEmail());
        created.setPassword(passwordEncoder.encode(request.getPassword()));
        created.setPhoneNumber(request.getPhoneNumber());
        Employee savedEmployee=employeeRepository.save(created);
        emailService.sendEmployeeID(savedEmployee.getEmail(), savedEmployee.getEmployeeId(), request.getPassword());
        return ResponseEntity.ok("EmployeeId and Password sent successfully.");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody OtpVerificationRequest request) throws AuthenticationBasedException, MessagingException {
        if(generatedOtp !=null && request.getOtp().equals(generatedOtp)){
            Employee created = new Employee();
            created.setEmployeeId(employeeService.generateUniqueUserId());
            created.setFirstName(firstName);
            created.setLastName(lastName);
            created.setEmail(email);
            created.setPassword(passwordEncoder.encode(password));
            created.setPhoneNumber(phoneNumber);

            Employee savedEmployee=employeeRepository.save(created);

            Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenProvider.generateToken(authentication);
            AuthResponse authResponse= new AuthResponse(token,true);

            Employee currentEmployee = employeeRepository.findByEmail(email);
            emailService.sendEmployeeID(currentEmployee.getEmail(), currentEmployee.getEmployeeId(),password);

            // Clear stored OTP and registered email after successful registration
            generatedOtp = null;
            email = null;
            firstName = null;
            lastName = null;



            return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);

        }else {
            // OTP verification failed
            return ResponseEntity.badRequest().body(new AuthResponse(null, false, "Invalid OTP. Registration failed."));
        }
    }
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody EmployeeLoginRequest employeeLoginRequest) {
        Authentication authentication;
        AuthResponse authResponse = new AuthResponse();

        String username = employeeLoginRequest.getEmail() != null
                ? employeeLoginRequest.getEmail()
                : employeeLoginRequest.getEmployeeId();
        String password = employeeLoginRequest.getEmployeePassword();

        authentication = employeeLoginRequest.getEmail() != null
                ? authenticateEmail(username, password)
                : authenticate(username, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        authResponse.setStatus(true);
        authResponse.setJwt(token);
        return ResponseEntity.ok(authResponse);
    }

    private Authentication authenticateEmail(String email, String password) {
        UserDetails userDetails = customEmployeeDetails.loadUserByUserEmail(email);
        System.out.println("sign in userDetails - "+userDetails);

        if (userDetails == null) {
            System.out.println("sign in userDetails - null " + userDetails);
            throw new BadCredentialsException("Invalid email or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            System.out.println("sign in userDetails - password not match " + userDetails);
            throw new BadCredentialsException("Invalid email or password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
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

    //admin side dashboard

    @GetMapping("/getEmployeesCount")
    public ResponseEntity<Long> getEmployeesCount(@RequestHeader("Authorization") String jwt){
        Long getEmployees=employeeService.getEmployeesCount();
        return new ResponseEntity<>(getEmployees,HttpStatus.OK);}

}
