package com.example.studentms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.studentms.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Page<Student> findByNameContainingOrStudentIdContaining(String nameKeyword, String idKeyword, Pageable pageable);
    Page<Student> findByIdIn(List<Long> ids, Pageable pageable);
    Page<Student> findByNameContainingAndIdInOrStudentIdContainingAndIdIn(
            String nameKeyword, List<Long> ids1, String idKeyword, List<Long> ids2, Pageable pageable);
}