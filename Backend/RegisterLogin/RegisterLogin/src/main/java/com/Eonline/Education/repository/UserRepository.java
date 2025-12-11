//package com.Eonline.Education.repository;
//
//import com.Eonline.Education.modals.User;
//import com.Eonline.Education.user.UserStatus;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface UserRepository extends JpaRepository<User, Long> {
//    // ✅ Correct: Returns Optional<User>
//    Optional<User> findByEmail(String email);
//
//    boolean existsByEmail(String email);
//
//    List<User> findAllByOrderByCreatedAtDesc();
//
//    // ✅ Fixed: Should return List<User> not Collection<Object>
//    List<User> findAllByStatus(UserStatus userStatus);
//
//    // Other methods...
//}





package com.Eonline.Education.repository;

import com.Eonline.Education.modals.User;
import com.Eonline.Education.user.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findAllByOrderByCreatedAtDesc();
    List<User> findAllByStatus(UserStatus userStatus);

    List<User> findByPasswordIsNull();
}