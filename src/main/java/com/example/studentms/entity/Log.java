package com.example.studentms.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "log")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String operation;
    private String detail;
    private String ip;
    private LocalDateTime createTime = LocalDateTime.now();
}