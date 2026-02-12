package com.Eonline.Education.Service;

import com.Eonline.Education.modals.TraineeCredentialGenerator;
import com.Eonline.Education.repository.TraineeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerTraineeDetails implements UserDetailsService {

    private final TraineeRepository traineeRepository;

    @Autowired
    public CustomerTraineeDetails(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TraineeCredentialGenerator trainee = traineeRepository.findByUserId(username);

        if (trainee == null) {
            throw new UsernameNotFoundException("Trainee not found with username: " + username);
        }

        List<GrantedAuthority> authorities = new ArrayList<>(); // You can add authorities if needed

        return new org.springframework.security.core.userdetails.User(trainee.getUserId(), trainee.getPassword(), authorities);
    }

    public UserDetails loadUserByEmail(String email) {
        TraineeCredentialGenerator trainee = traineeRepository.findByEmail(email);

        if (trainee == null) {
            throw new UsernameNotFoundException("Trainee not found with Email: " + email);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        return new org.springframework.security.core.userdetails.User(trainee.getEmail(), trainee.getPassword(), authorities);
    }
}


//package com.Eonline.Education.Service;
//
//import com.Eonline.Education.modals.TraineeCredentialGenerator;
//
//import com.Eonline.Education.repository.TraineeRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//@Service
//public class CustomerTraineeDetails implements UserDetailsService {
//
//    @Autowired
//    private TraineeRepository traineeRepository;
//
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        TraineeCredentialGenerator user = traineeRepository.findByUserName(username);
//
//        if(user == null) {
//            throw new UsernameNotFoundException("trainee not found with email "+username);
//        }
//
//        List<GrantedAuthority> authorities = new ArrayList<>();
//
//        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),authorities);
//    }
//}