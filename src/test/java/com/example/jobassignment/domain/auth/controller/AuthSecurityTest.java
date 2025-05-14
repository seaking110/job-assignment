package com.example.jobassignment.domain.auth.controller;

import com.example.jobassignment.Exception.CustomException;
import com.example.jobassignment.Exception.ErrorCode;
import com.example.jobassignment.config.JwtUtil;
import com.example.jobassignment.domain.auth.dto.response.SignUpResponse;
import com.example.jobassignment.domain.auth.enums.UserRole;
import com.example.jobassignment.domain.auth.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthSecurityTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("관리자 권한 부여 - 성공")
    void grantAdmin_success() throws Exception {
        // given
        Long userId = 2L;
        String token = jwtUtil.createToken(1L, "admin", UserRole.ROLE_ADMIN);
        SignUpResponse response = new SignUpResponse("seaking1", "나유류", UserRole.ROLE_ADMIN);

        given(authService.grantAdminRole(anyLong())).willReturn(response);
        // when & then
        mockMvc.perform(patch("/admin/users/{userId}/roles", userId)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("seaking1"))
                .andExpect(jsonPath("$.nickname").value("나유류"))
                .andExpect(jsonPath("$.role").value("ROLE_ADMIN"));
    }

    @Test
    @DisplayName("일반 사용자의 권한 부여 요청 - 실패")
    void grantAdmin_user_forbidden() throws Exception {
        // given
        Long userId = 3L;
        String token = jwtUtil.createToken(2L, "seaking1", UserRole.ROLE_USER);

        // when & then
        mockMvc.perform(patch("/admin/users/{userId}/roles", userId)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error.message").value("접근 권한이 없습니다."));
    }

    @Test
    @DisplayName("이미 관리자에게 권한 부여 요청 - 실패")
    void grantAdmin_alreadyAdmin() throws Exception {
        // given
        Long userId = 2L;
        String token = jwtUtil.createToken(1L, "admin", UserRole.ROLE_ADMIN);

        given(authService.grantAdminRole(anyLong())).willThrow(new CustomException(ErrorCode.ALREADY_ADMIN));
        // when & then
        mockMvc.perform(patch("/admin/users/{userId}/roles", userId)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value("이미 관리자입니다."));
    }

    @Test
    @DisplayName("없는 사용자에게 권한 부여 요청 - 실패")
    void grantAdmin_userNotFound() throws Exception {
        // given
        Long userId = 2L;
        String token = jwtUtil.createToken(1L, "admin", UserRole.ROLE_ADMIN);

        given(authService.grantAdminRole(anyLong())).willThrow(new CustomException(ErrorCode.INVALID_USER));
        // when & then
        mockMvc.perform(patch("/admin/users/{userId}/roles", userId)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.message").value("유효하지 않은 유저입니다."));
    }

    @Test
    @DisplayName("토큰 없이 요청 - 인증 실패")
    void grantAdmin_fail_noToken() throws Exception {
        // given
        Long userId = 2L;
        // when & then
        mockMvc.perform(patch("/admin/users/{userId}/roles", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error.message").value("유효하지 않은 인증 토큰입니다."));
    }

    @Test
    @DisplayName("만료된 토큰으로 요청 - 인증 실패")
    void grantAdmin_fail_expiredToken() throws Exception {
        // given
        Long userId = 2L;
        String token = jwtUtil.createTokenWithCustomExpiration(1L, "admin", UserRole.ROLE_ADMIN); // 만료된 토큰 생성
        // when & then
        mockMvc.perform(patch("/admin/users/{userId}/roles", userId)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error.message").value("만료된 JWT 토큰입니다."));
    }

    @Test
    @DisplayName("잘못된 형식의 토큰으로 요청 - 인증 실패")
    void grantAdmin_fail_malformedToken() throws Exception {
        // given
        Long userId = 2L;
        String token = "Bearer qwer";
        // when & then
        mockMvc.perform(patch("/admin/users/{userId}/roles", userId)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error.message").value("유효하지 않는 JWT 서명입니다."));
    }
}
