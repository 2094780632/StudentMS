package com.example.studentms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.studentms.entity.TeacherStudent;

public interface TeacherStudentRepository extends JpaRepository<TeacherStudent, Long> {
    List<TeacherStudent> findByTeacherId(Long teacherId);
    List<TeacherStudent> findByStudentId(Long studentId);
    boolean existsByTeacherIdAndStudentIdAndSubject(Long teacherId, Long studentId, String subject);
    void deleteByTeacherIdAndStudentIdAndSubject(Long teacherId, Long studentId, String subject);
    void deleteByStudentId(Long studentId);
}
