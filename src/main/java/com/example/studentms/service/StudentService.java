package com.example.studentms.service;

import com.example.studentms.entity.Student;
import com.example.studentms.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // 分页 + 关键词搜索
    public Page<Student> searchStudents(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        if (keyword == null || keyword.trim().isEmpty()) {
            return studentRepository.findAll(pageable);
        } else {
            return studentRepository.findByNameContainingOrStudentIdContaining(keyword, keyword, pageable);
        }
    }

    public Student findById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public void save(Student student) {
        studentRepository.save(student);
    }

    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }
}