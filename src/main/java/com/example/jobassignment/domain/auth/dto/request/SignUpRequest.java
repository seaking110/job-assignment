package com.example.jobassignment.domain.auth.dto.request;


import com.example.jobassignment.domain.auth.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpRequest {
    private String username;

    private String password;

    private String nickname;

}
