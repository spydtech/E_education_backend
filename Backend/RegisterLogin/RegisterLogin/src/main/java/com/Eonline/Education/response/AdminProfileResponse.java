package com.Eonline.Education.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminProfileResponse {
    private String profilePhoto;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
}
