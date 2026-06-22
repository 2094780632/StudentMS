package com.example.studentms.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.studentms.entity.Log;
import com.example.studentms.repository.LogRepository;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public void save(Log log) {
        logRepository.save(log);
    }

    public Page<Log> findByUsername(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        if (username == null || username.trim().isEmpty()) {
            return logRepository.findAll(pageable);
        } else {
            return logRepository.findByUsernameContaining(username, pageable);
        }
    }

    public long countToday() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();
        return logRepository.countByCreateTimeBetween(start, end);
    }

    public List<Log> findLatest(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("createTime").descending());
        return logRepository.findAll(pageable).getContent();
    }
}