package com.example.jobassignment.domain.auth.entity;

import com.example.jobassignment.domain.auth.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private UserRole role;

    public void updateRole() {
        role = UserRole.ROLE_ADMIN;
    }
}
