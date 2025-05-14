package com.example.jobassignment.config;

import com.example.jobassignment.domain.auth.dto.AuthUser;
import com.example.jobassignment.domain.auth.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        String uri = request.getRequestURI();

        if (uri.startsWith("/swagger-ui")
                || uri.startsWith("/v3/api-docs")
                || uri.startsWith("/swagger")
                || uri.startsWith("/webjars")
                || uri.startsWith("/favicon")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = jwtUtil.substringToken(authorizationHeader);
            try {
                Claims claims = jwtUtil.extractClaims(jwt);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    setAuthentication(claims);
                }
            } catch (SecurityException | MalformedJwtException e) {
                writeJsonError(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "INVALID_TOKEN", "유효하지 않는 JWT 서명입니다.");
            } catch (ExpiredJwtException e) {
                writeJsonError(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "EXPIRED_TOKEN", "만료된 JWT 토큰입니다.");
            } catch (UnsupportedJwtException e) {
                writeJsonError(response, HttpServletResponse.SC_BAD_REQUEST,
                        "UNSUPPORTED_TOKEN", "지원되지 않는 JWT 토큰입니다.");
            } catch (IllegalArgumentException e) {
                writeJsonError(response, HttpServletResponse.SC_BAD_REQUEST,
                        "EMPTY_TOKEN", "JWT 토큰이 존재하지 않습니다.");
            }
        }
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(Claims claims) {
        Long userId = Long.valueOf(claims.getSubject());
        String username = claims.get("username", String.class);
        UserRole userRole = UserRole.of(claims.get("userRole", String.class));

        AuthUser authUser = new AuthUser(userId, username, userRole);
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private void writeJsonError(HttpServletResponse response, int status, String code, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String body = String.format("""
            {
              "error": {
                "code": "%s",
                "message": "%s"
              }
            }
        """, code, message);

        response.getWriter().write(body);
    }
}
