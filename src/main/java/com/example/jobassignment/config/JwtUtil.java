package com.example.jobassignment.config;

import com.example.jobassignment.Exception.CustomException;
import com.example.jobassignment.Exception.ErrorCode;
import com.example.jobassignment.domain.auth.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final long TOKEN_TIME = 120 * 60 * 1000L;

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(jwtSecretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성
    public String createToken(Long userId, String username, UserRole userRole) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(userId))
                        .claim("username", username)
                        .claim("userRole", userRole.name())
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    // 토큰 추출
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        throw new CustomException(ErrorCode.INVALID_TOKEN);
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 만료된 토큰 생성
    public String createTokenWithCustomExpiration(Long userId, String username, UserRole role) {
        Date now = new Date();
        Date expiredAt = new Date(now.getTime() - 5000);
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(userId))
                        .claim("username", username)
                        .claim("userRole", role.name())
                        .setExpiration(expiredAt)
                        .setIssuedAt(new Date(now.getTime() - 10000))
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }
}
