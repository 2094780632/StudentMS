package com.example.studentms.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.studentms.entity.Log;

public interface LogRepository extends JpaRepository<Log, Long> {
    Page<Log> findByUsernameContaining(String username, Pageable pageable);
    long countByCreateTimeBetween(LocalDateTime start, LocalDateTime end);
}