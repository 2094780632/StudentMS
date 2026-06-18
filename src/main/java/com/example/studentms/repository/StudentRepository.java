package com.example.studentms.repository;

import com.example.studentms.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Page<Student> findByNameContainingOrStudentIdContaining(String nameKeyword, String idKeyword, Pageable pageable);
}