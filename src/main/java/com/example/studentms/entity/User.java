package com.example.studentms.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;
    private String fullname;
    private String role;   // 存储 "ROLE_ADMIN" 或 "ROLE_USER"

    @Column(columnDefinition = "VARCHAR(20) DEFAULT 'TEACHER'")
    private String identity;  // TEACHER / STUDENT

    private Boolean enabled = true;
    private LocalDateTime createTime = LocalDateTime.now();
}