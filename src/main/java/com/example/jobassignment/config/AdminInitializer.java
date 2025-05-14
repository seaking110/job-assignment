package com.example.jobassignment.config;

import com.example.jobassignment.domain.auth.enums.UserRole;
import com.example.jobassignment.domain.auth.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${admin.password}")
    private String password;

    @PostConstruct
    public void initAdmin() {
        if (!userRepository.existsByUsername("admin")) {
            String encodedPassword = passwordEncoder.encode(password);
            userRepository.save("admin", encodedPassword, "관리자", UserRole.ROLE_ADMIN);
        }
    }
}
