package com.Eonline.Education.modals;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotBlank(message = "Full name is required")
    @Length(max = 100, message = "Full name must be less than 100 characters")
    @Column(name = "full_name")
    private String fullName;

//    @NotBlank(message = "Location is required")
    @Column(name = "location")
    private String location;

//    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{10,15}", message = "Phone number must be between 10 and 15 digits")
    @Column(name = "phone_number")
    private String phoneNumber;
//
//    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(name = "user_email", unique = true)
    private String userEmail;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
