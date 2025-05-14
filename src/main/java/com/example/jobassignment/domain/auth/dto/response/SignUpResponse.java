package com.example.jobassignment.domain.auth.dto.response;

import com.example.jobassignment.domain.auth.entity.User;
import com.example.jobassignment.domain.auth.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SignUpResponse {
    @Schema(description = "사용자 아이디", example = "seaking123")
    private String username;

    @Schema(description = "닉네임", example = "나유류")
    private String nickname;

    @Schema(description = "사용자 권한", example = "ROLE_USER")
    private UserRole roles;

    public static SignUpResponse of(User user) {
        return new SignUpResponse(user.getUsername(), user.getNickname(), user.getRole());
    }

}
