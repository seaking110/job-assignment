package com.example.jobassignment.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequest {
    @Schema(description = "사용자 아이디", example = "seaking123")
    private String username;

    @Schema(description = "비밀번호", example = "1234")
    private String password;
}
