package com.example.studentms.util;

import com.example.studentms.entity.User;
import com.example.studentms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserUtil {

    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElse(null);
    }

    public String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public boolean isAdmin() {
        User user = getCurrentUser();
        return user != null && "ROLE_ADMIN".equals(user.getRole());
    }

    public boolean isTeacher() {
        User user = getCurrentUser();
        return user != null && "TEACHER".equals(user.getIdentity());
    }

    public boolean isStudent() {
        User user = getCurrentUser();
        return user != null && "STUDENT".equals(user.getIdentity());
    }

    public Long getCurrentUserId() {
        User user = getCurrentUser();
        return user != null ? user.getId() : null;
    }
}
