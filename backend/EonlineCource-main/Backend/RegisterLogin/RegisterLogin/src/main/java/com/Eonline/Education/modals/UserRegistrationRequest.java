//package com.Eonline.Education.modals;
//
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Size;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class UserRegistrationRequest {
//
//    @NotBlank(message = "User name is required")
//    private String userName;
//
//    @NotBlank(message = "Email is required")
//    @Email(message = "Invalid email format")
//    private String email;
//
//    @NotBlank(message = "Password is required")
//    @Size(min = 6, message = "Password must be at least 6 characters")
//    private String password;
//
//    // Constructors, getters, and setters
//}

package com.Eonline.Education.modals;

import lombok.*;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequest {
    private String firstName;
    private String LastName;
    private String email;
    private String password;
    private String role;

    // Constructors, getters, and setters
}