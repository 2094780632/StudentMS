package com.example.studentms.repository;

import com.example.studentms.entity.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
    Page<Log> findByUsernameContaining(String username, Pageable pageable);
}