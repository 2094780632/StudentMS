package com.example.studentms.service;

import com.example.studentms.entity.Student;
import com.example.studentms.entity.User;
import com.example.studentms.repository.StudentRepository;
import com.example.studentms.util.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Autowired
    private TeacherStudentService teacherStudentService;

    // 分页 + 关键词搜索（多对多数据隔离）
    public Page<Student> searchStudents(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        User currentUser = currentUserUtil.getCurrentUser();
        if (currentUser == null) {
            return Page.empty();
        }
        // 管理员：查看所有
        if (currentUserUtil.isAdmin()) {
            if (keyword == null || keyword.trim().isEmpty()) {
                return studentRepository.findAll(pageable);
            } else {
                return studentRepository.findByNameContainingOrStudentIdContaining(keyword, keyword, pageable);
            }
        }
        // 教师：查看自己负责的所有学生
        if (currentUserUtil.isTeacher()) {
            List<Long> studentIds = teacherStudentService.getStudentIdsByTeacherId(currentUser.getId());
            if (studentIds.isEmpty()) return Page.empty();
            if (keyword == null || keyword.trim().isEmpty()) {
                return studentRepository.findByIdIn(studentIds, pageable);
            } else {
                return studentRepository.findByNameContainingAndIdInOrStudentIdContainingAndIdIn(
                        keyword, studentIds, keyword, studentIds, pageable);
            }
        }
        // 学生身份：返回空
        return Page.empty();
    }

    public Student findById(Long id) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null || !hasPermission(student)) {
            return null;
        }
        return student;
    }

    public List<Student> findAllByIds(List<Long> ids) {
        return studentRepository.findAllById(ids);
    }

    public void save(Student student) {
        studentRepository.save(student);
    }

    public void deleteById(Long id) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null && hasPermission(student)) {
            studentRepository.deleteById(id);
        }
    }

    public long countAll() {
        return studentRepository.count();
    }

    public long countByTeacherId(Long teacherId) {
        return teacherStudentService.countStudentsByTeacherId(teacherId);
    }

    private boolean hasPermission(Student student) {
        User currentUser = currentUserUtil.getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        if (currentUserUtil.isAdmin()) {
            return true;
        }
        if (currentUserUtil.isTeacher()) {
            List<Long> myStudentIds = teacherStudentService.getStudentIdsByTeacherId(currentUser.getId());
            return myStudentIds.contains(student.getId());
        }
        return false;
    }
}