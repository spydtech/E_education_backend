package com.Eonline.Education.Service;

import com.Eonline.Education.modals.Employee;
import com.Eonline.Education.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomEmployeeDetails implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;


    @Override
    public UserDetails loadUserByUsername(String employeeId) throws UsernameNotFoundException {

        Employee employee = employeeRepository.findByEmployeeId(employeeId);

        if(employee == null) {
            throw new UsernameNotFoundException("Employee not found with EmployeeId: "+employeeId);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        return new org.springframework.security.core.userdetails.User(employee.getEmployeeId(),employee.getPassword(),authorities);
    }

    public UserDetails loadUserByUserEmail(String email) {
        Employee employee = employeeRepository.findByEmail(email);

        if(employee == null) {
            throw new UsernameNotFoundException("Employee not found with email: "+email);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        return new org.springframework.security.core.userdetails.User(employee.getEmail(),employee.getPassword(),authorities);
    }
}
