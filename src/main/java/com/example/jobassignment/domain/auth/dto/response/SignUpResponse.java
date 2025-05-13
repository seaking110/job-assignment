package com.example.jobassignment.domain.auth.dto.response;

import com.example.jobassignment.domain.auth.entity.User;
import com.example.jobassignment.domain.auth.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SignUpResponse {
    private String username;

    private String nickname;

    private UserRole roles;

    public static SignUpResponse of(User user) {
        return new SignUpResponse(user.getUsername(), user.getNickname(), user.getRole());
    }

}
