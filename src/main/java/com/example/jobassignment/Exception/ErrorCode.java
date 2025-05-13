package com.example.jobassignment.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_ALREADY_EXISTS("이미 가입된 사용자입니다.", BAD_REQUEST),
    ALREADY_ADMIN("이미 관리자입니다.", BAD_REQUEST),
    INVALID_CREDENTIALS("아이디 또는 비밀번호가 올바르지 않습니다.", UNAUTHORIZED),
    INVALID_TOKEN("유효하지 않은 인증 토큰입니다.", UNAUTHORIZED),
    INVALID_USER("유효하지 않은 유저입니다.", NOT_FOUND),
    INVALID_ROLE("유효하지 않은 권한입니다.", UNAUTHORIZED);


    private final String message;
    private final HttpStatus status;
}
