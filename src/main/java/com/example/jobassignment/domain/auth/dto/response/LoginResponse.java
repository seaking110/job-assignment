package com.example.jobassignment.domain.auth.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    @Schema(description = "JWT 토큰", example = "Bearer eyJhb...")
    private final String token;
}
