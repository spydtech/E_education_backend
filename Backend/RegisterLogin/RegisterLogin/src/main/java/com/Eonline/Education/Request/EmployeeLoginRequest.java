package com.Eonline.Education.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeLoginRequest {

    private String employeeId;

    private String employeePassword;
}
