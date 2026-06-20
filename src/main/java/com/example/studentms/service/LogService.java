package com.example.studentms.service;

import com.example.studentms.entity.Log;
import com.example.studentms.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
}