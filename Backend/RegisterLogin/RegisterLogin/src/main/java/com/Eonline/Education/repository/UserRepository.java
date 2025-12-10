package com.Eonline.Education.repository;

import java.util.List;
import java.util.Optional;

import com.Eonline.Education.modals.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);
     List<User> findAllByOrderByCreatedAtDesc();

    boolean existsByEmail(String userEmail);

}
