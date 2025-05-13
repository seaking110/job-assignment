package com.example.jobassignment.domain.auth.service;

import com.example.jobassignment.Exception.CustomException;
import com.example.jobassignment.Exception.ErrorCode;
import com.example.jobassignment.config.JwtUtil;
import com.example.jobassignment.domain.auth.dto.AuthUser;
import com.example.jobassignment.domain.auth.dto.request.LoginRequest;
import com.example.jobassignment.domain.auth.dto.request.SignUpRequest;
import com.example.jobassignment.domain.auth.dto.response.LoginResponse;
import com.example.jobassignment.domain.auth.dto.response.SignUpResponse;
import com.example.jobassignment.domain.auth.entity.User;
import com.example.jobassignment.domain.auth.enums.UserRole;
import com.example.jobassignment.domain.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public SignUpResponse signup(SignUpRequest signUpRequest) {
        // username 중복 시 예외 처리
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        User user = userRepository.save(signUpRequest.getUsername(), encodedPassword, signUpRequest.getNickname(), UserRole.ROLE_USER);

        return SignUpResponse.of(user);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(()-> new CustomException(ErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        return new LoginResponse(jwtUtil.createToken(user.getId(), user.getUsername(), user.getRole()));
    }

    public SignUpResponse grantAdminRole(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new CustomException(ErrorCode.INVALID_USER));
        if (user.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new CustomException(ErrorCode.ALREADY_ADMIN);
        }
        user.updateRole();
        return SignUpResponse.of(user);
    }

    public List<SignUpResponse> getUsers() {
        return userRepository.findAll().stream()
                .map(SignUpResponse::of)
                .toList();
    }
}
