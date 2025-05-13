package com.example.jobassignment.domain.auth.controller;

import com.example.jobassignment.domain.auth.dto.AuthUser;
import com.example.jobassignment.domain.auth.dto.request.LoginRequest;
import com.example.jobassignment.domain.auth.dto.request.SignUpRequest;
import com.example.jobassignment.domain.auth.dto.response.LoginResponse;
import com.example.jobassignment.domain.auth.dto.response.SignUpResponse;
import com.example.jobassignment.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    private ResponseEntity<SignUpResponse> signup(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(authService.signup(signUpRequest));
    }

    @PostMapping("/login")
    private ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PatchMapping("/admin/users/{userId}/roles")
    private ResponseEntity<SignUpResponse> grantAdminRole(@PathVariable Long userId) {
        return ResponseEntity.ok(authService.grantAdminRole(userId));
    }

    @GetMapping("/users")
    private ResponseEntity<List<SignUpResponse>> getUsers() {
        return ResponseEntity.ok(authService.getUsers());
    }
}
