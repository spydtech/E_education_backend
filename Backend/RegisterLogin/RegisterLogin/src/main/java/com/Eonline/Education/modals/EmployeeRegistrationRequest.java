package com.Eonline.Education.modals;

import lombok.*;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRegistrationRequest {
    private String name;
    private String email;
    private String password;
}
