package com.example.jobassignment.domain.auth.controller;

import com.example.jobassignment.Exception.CustomException;
import com.example.jobassignment.Exception.ErrorCode;
import com.example.jobassignment.config.JwtUtil;
import com.example.jobassignment.domain.auth.dto.request.LoginRequest;
import com.example.jobassignment.domain.auth.dto.request.SignUpRequest;
import com.example.jobassignment.domain.auth.dto.response.LoginResponse;
import com.example.jobassignment.domain.auth.dto.response.SignUpResponse;
import com.example.jobassignment.domain.auth.enums.UserRole;
import com.example.jobassignment.domain.auth.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(JwtUtil.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {
    // 필터 x 테스트
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;


    @Test
    @DisplayName("정상 회원가입 요청")
    void signup_success() throws Exception {
        //given
        SignUpRequest request = new SignUpRequest("seaking1", "1234", "나유류");

        given(authService.signup(any())).willReturn(new SignUpResponse("seaking1", "나유류", UserRole.ROLE_USER));
        //when & then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("seaking1"))
                .andExpect(jsonPath("$.nickname").value("나유류"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"));
    }

    @Test
    @DisplayName("이메일 중복으로 회원가입 실패")
    void signup_emailDuplicated() throws Exception {
        // given
        SignUpRequest request = new SignUpRequest("seaking1", "1234", "나유류");

        given(authService.signup(any())).willThrow(new CustomException(ErrorCode.USER_ALREADY_EXISTS));

        // when & then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value("이미 가입된 사용자입니다."));
    }

    @Test
    @DisplayName("정상 로그인 요청")
    void login_success() throws Exception {
        // given
        LoginRequest request = new LoginRequest("seaking1", "1234");

        given(authService.login(any())).willReturn(new LoginResponse("Bearer eyJhbG..."));
        // when & then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("Bearer eyJhbG..."));
    }

    @Test
    @DisplayName("존재하지 않는 유저네임으로 로그인 실패")
    void login_usernameNotFound() throws Exception {
        // given
        LoginRequest request = new LoginRequest("seaking1", "1234");

        given(authService.login(any())).willThrow(new CustomException(ErrorCode.INVALID_USER));
        // when & then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.message").value("유효하지 않은 유저입니다."));
    }

    @Test
    @DisplayName("비밀번호 불일치로 로그인 실패")
    void login_wrongPassword() throws Exception {
        // given
        LoginRequest request = new LoginRequest("seaking1", "1234");

        given(authService.login(any())).willThrow(new CustomException(ErrorCode.INVALID_CREDENTIALS));
        // when & then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error.message").value("아이디 또는 비밀번호가 올바르지 않습니다."));
    }
}