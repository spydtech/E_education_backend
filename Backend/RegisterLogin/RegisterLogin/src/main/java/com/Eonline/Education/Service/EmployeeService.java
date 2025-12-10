package com.Eonline.Education.Service;

import com.Eonline.Education.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    private static final String PREFIX = "edu";

    public String generateUniqueUserId() {
        Random random = new Random();
        String userId;
        do {
            int uniqueNumber = 001 + random.nextInt(999);
            userId = PREFIX + uniqueNumber;
        } while (employeeRepository.existsByEmployeeId(userId));
        return userId;
    }
}
