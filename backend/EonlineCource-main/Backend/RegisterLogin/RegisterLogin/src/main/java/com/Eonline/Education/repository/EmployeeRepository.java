package com.Eonline.Education.repository;

import com.Eonline.Education.modals.Employee;
import com.Eonline.Education.modals.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    boolean existsByEmail(String email);

    boolean existsByEmployeeId(String userId);

    Employee findByEmployeeId(String employeeId);

    Employee findByEmail(String employeeEmail);
}
