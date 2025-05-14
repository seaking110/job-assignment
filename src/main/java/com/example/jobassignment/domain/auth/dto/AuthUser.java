package com.example.jobassignment.domain.auth.dto;

import com.example.jobassignment.domain.auth.enums.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
public class AuthUser {
    private final Long userId;
    private final String username;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUser(Long userId, String username, UserRole role) {
        this.userId = userId;
        this.username = username;
        this.authorities = List.of(new SimpleGrantedAuthority(role.name()));
    }


}