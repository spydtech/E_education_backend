package com.Eonline.Education.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse implements Serializable {

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must be less than 100 characters")
    private String fullName;

    @Size(max = 255, message = "Location must be less than 255 characters")
    private String location;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String userEmail;

    @Size(max = 20, message = "Phone number must be less than 20 characters")
    private String phoneNumber;
}
