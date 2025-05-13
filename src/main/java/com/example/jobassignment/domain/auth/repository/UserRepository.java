package com.example.jobassignment.domain.auth.repository;

import com.example.jobassignment.domain.auth.entity.User;
import com.example.jobassignment.domain.auth.enums.UserRole;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository {
    // 실제 환경이라면 ConcurrentHashMap 사용
    private Map<Long, User> userMap = new HashMap<>();
    private Long userId = 1L;
    public boolean existsByUsername(String username) {
        return userMap.values().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    public User save(String username, String encodedPassword, String nickname, UserRole userRole) {
        User user = new User(userId++, username, encodedPassword, nickname, userRole);
        userMap.put(user.getId(), user);
        return user;
    }

    public Optional<User> findByUsername(String username) {
        return userMap.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    public Optional<User> findById(Long userId) {
        return Optional.ofNullable(userMap.get(userId));
    }

    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }
}
