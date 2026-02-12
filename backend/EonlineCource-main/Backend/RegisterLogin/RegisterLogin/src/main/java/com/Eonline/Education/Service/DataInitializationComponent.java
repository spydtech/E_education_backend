package com.Eonline.Education.Service;

import com.Eonline.Education.modals.User;
import com.Eonline.Education.repository.UserRepository;
import com.Eonline.Education.user.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializationComponent implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializationComponent.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializationComponent(UserRepository userRepository,
                                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        initializeAdminUser();
    }

    private void initializeAdminUser() {
        String adminEmail = "support@e-education.in";
        String adminPassword = "Spyd@1234";
        String adminFirstName = "Spyd";
        String adminLastName = "tech";

        try {
            // ✅ FIXED: Check if admin exists using Optional
            Optional<User> existingAdmin = userRepository.findByEmail(adminEmail);

            if (existingAdmin.isEmpty()) {
                User adminUser = new User();
                adminUser.setFirstName(adminFirstName);
                adminUser.setLastName(adminLastName);
                adminUser.setEmail(adminEmail);
                adminUser.setPassword(passwordEncoder.encode(adminPassword));
                adminUser.setRole("ADMIN");
                adminUser.setStatus(UserStatus.ACTIVE); // Make sure to set status

                userRepository.save(adminUser);
                logger.info("✅ Admin user initialized successfully. Email: {}, Password: {}", adminEmail, adminPassword);
            } else {
                logger.warn("⚠️ Admin user already exists. Email: {}", adminEmail);
            }
        } catch (DataAccessException e) {
            logger.error("❌ Data access error while initializing admin user: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("❌ Error initializing admin user: " + e.getMessage(), e);
        }
    }
}