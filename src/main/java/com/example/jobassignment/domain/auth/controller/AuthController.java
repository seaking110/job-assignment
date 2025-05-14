package com.example.jobassignment.domain.auth.controller;

import com.example.jobassignment.Exception.ErrorResponse;
import com.example.jobassignment.domain.auth.dto.AuthUser;
import com.example.jobassignment.domain.auth.dto.request.LoginRequest;
import com.example.jobassignment.domain.auth.dto.request.SignUpRequest;
import com.example.jobassignment.domain.auth.dto.response.LoginResponse;
import com.example.jobassignment.domain.auth.dto.response.SignUpResponse;
import com.example.jobassignment.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "사용자 정보를 입력하여 회원가입 수행")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "이미 존재하는 사용자", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    private ResponseEntity<SignUpResponse> signup(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(authService.signup(signUpRequest));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "username과 password를 입력하여 JWT 토큰 발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "아이디 또는 비밀번호가 올바르지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    private ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PatchMapping("/admin/users/{userId}/roles")
    @Operation(summary = "admin 권한 부여", description = "다른 사용자에게 admin 권한 부여")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "관리자 권한 부여 성공"),
            @ApiResponse(responseCode = "400", description = "이미 관리자입니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "유효하지 않은 유저입니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    private ResponseEntity<SignUpResponse> grantAdminRole(@PathVariable Long userId) {
        return ResponseEntity.ok(authService.grantAdminRole(userId));
    }

    @GetMapping("/users")
    @Operation(summary = "전체 유저 조회", description = "전체 유저 목록 조회 (확인 용)")
    private ResponseEntity<List<SignUpResponse>> getUsers() {
        return ResponseEntity.ok(authService.getUsers());
    }

    @GetMapping("/swagger")
    public RedirectView redirectToSwagger() {
        return new RedirectView("/swagger-ui/index.html");
    }
}
