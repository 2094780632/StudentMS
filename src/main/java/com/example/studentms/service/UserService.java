package com.example.studentms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.studentms.entity.User;
import com.example.studentms.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 按用户名模糊搜索，分页，按 id 降序
    public Page<User> findUsers(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        if (keyword == null || keyword.trim().isEmpty()) {
            return userRepository.findAll(pageable);
        } else {
            return userRepository.findByUsernameContaining(keyword, pageable);
        }
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void saveUser(User user) {
        if (user.getId() == null) {
            // 新增：直接保存明文密码
        } else {
            // 编辑：若 password 为空或 null，从数据库查出原密码回填
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                User existing = userRepository.findById(user.getId()).orElse(null);
                if (existing != null) {
                    user.setPassword(existing.getPassword());
                }
            }
            // 密码非空则直接保存（明文）
        }
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public long countAll() {
        return userRepository.count();
    }

    public long countByIdentity(String identity) {
        return userRepository.countByIdentity(identity);
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElse(null);
    }

    public List<User> findTeachers() {
        return userRepository.findByIdentity("TEACHER");
    }

    public Map<Long, String> getTeacherNameMap() {
        Map<Long, String> map = new HashMap<>();
        List<User> teachers = findTeachers();
        for (User t : teachers) {
            map.put(t.getId(), t.getFullname());
        }
        return map;
    }
}
