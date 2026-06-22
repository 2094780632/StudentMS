package com.example.studentms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.studentms.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Page<User> findByUsernameContaining(String keyword, Pageable pageable);
    long countByIdentity(String identity);
    List<User> findByIdentity(String identity);
}