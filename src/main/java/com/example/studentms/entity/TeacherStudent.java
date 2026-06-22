package com.example.studentms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(name = "teacher_student", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"teacher_id", "student_id", "subject"})
})
public class TeacherStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "teacher_id")
    private Long teacherId;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "subject")
    private String subject;  // 科目名称，如 "数学"、"语文"
}
