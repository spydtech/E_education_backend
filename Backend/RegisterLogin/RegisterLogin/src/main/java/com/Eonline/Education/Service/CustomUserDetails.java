//package com.Eonline.Education.Service;
//
//import com.Eonline.Education.modals.User;
//import com.Eonline.Education.repository.UserRepository;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class CustomUserDetails implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    public CustomUserDetails(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        // ✅ FIXED: Get User from Optional using orElseThrow
//        User user = userRepository.findByEmail(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
//
//        // Create authorities
//        List<GrantedAuthority> authorities = new ArrayList<>();
//
//        if (user.getRole() != null && !user.getRole().isEmpty()) {
//            String role = user.getRole().toUpperCase();
//            if (!role.startsWith("ROLE_")) {
//                role = "ROLE_" + role;
//            }
//            authorities.add(new SimpleGrantedAuthority(role));
//        }
//
//        // Return UserDetails
//        return new org.springframework.security.core.userdetails.User(
//                user.getEmail(),
//                user.getPassword(),
//                authorities
//        );
//    }
//}





package com.Eonline.Education.Service;

import com.Eonline.Education.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Primary
public class CustomUserDetails implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority(
                                user.getRole().startsWith("ROLE_") ? user.getRole() : "ROLE_" + user.getRole()
                        ))
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}