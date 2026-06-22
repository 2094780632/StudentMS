package com.example.studentms.service;

import com.example.studentms.entity.Student;
import com.example.studentms.entity.TeacherStudent;
import com.example.studentms.entity.User;
import com.example.studentms.repository.StudentRepository;
import com.example.studentms.repository.TeacherStudentRepository;
import com.example.studentms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeacherStudentService {

    @Autowired
    private TeacherStudentRepository tsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    // 科目列表
    public static final List<String> SUBJECTS = Arrays.asList(
        "数学", "语文", "英语", "物理", "化学", "生物", "历史", "地理", "政治"
    );

    /** 获取某学生的所有科任老师 */
    public List<User> getTeachersByStudentId(Long studentId) {
        List<TeacherStudent> relations = tsRepository.findByStudentId(studentId);
        List<Long> teacherIds = relations.stream().map(TeacherStudent::getTeacherId).distinct().collect(Collectors.toList());
        if (teacherIds.isEmpty()) return Collections.emptyList();
        return userRepository.findAllById(teacherIds);
    }

    /** 获取某老师负责的所有学生ID */
    public List<Long> getStudentIdsByTeacherId(Long teacherId) {
        return tsRepository.findByTeacherId(teacherId).stream()
                .map(TeacherStudent::getStudentId).distinct().collect(Collectors.toList());
    }

    /** 获取某老师负责的所有学生 */
    public List<Student> getStudentsByTeacherId(Long teacherId) {
        List<Long> ids = getStudentIdsByTeacherId(teacherId);
        if (ids.isEmpty()) return Collections.emptyList();
        return studentRepository.findAllById(ids);
    }

    /** 获取某学生的 (teacherId -> 科目列表) 映射 */
    public Map<Long, List<String>> getTeacherSubjectMap(Long studentId) {
        List<TeacherStudent> relations = tsRepository.findByStudentId(studentId);
        return relations.stream().collect(Collectors.groupingBy(
                TeacherStudent::getTeacherId,
                Collectors.mapping(TeacherStudent::getSubject, Collectors.toList())
        ));
    }

    /** 获取某学生的关联记录（用于显示） */
    public List<TeacherStudent> getRelationsByStudentId(Long studentId) {
        return tsRepository.findByStudentId(studentId);
    }

    /** 获取某学生的科目+老师详情（教师姓名+科目） */
    public List<Map<String, String>> getTeacherSubjectDetails(Long studentId) {
        List<TeacherStudent> relations = tsRepository.findByStudentId(studentId);
        List<Map<String, String>> result = new ArrayList<>();
        for (TeacherStudent ts : relations) {
            User teacher = userRepository.findById(ts.getTeacherId()).orElse(null);
            Map<String, String> item = new HashMap<>();
            item.put("teacherName", teacher != null ? teacher.getFullname() : "未知");
            item.put("subject", ts.getSubject());
            item.put("teacherId", String.valueOf(ts.getTeacherId()));
            result.add(item);
        }
        return result;
    }

    /** 分配老师-学生-科目关系 */
    public void assignTeacherToStudent(Long teacherId, Long studentId, String subject) {
        if (!tsRepository.existsByTeacherIdAndStudentIdAndSubject(teacherId, studentId, subject)) {
            TeacherStudent ts = new TeacherStudent();
            ts.setTeacherId(teacherId);
            ts.setStudentId(studentId);
            ts.setSubject(subject);
            tsRepository.save(ts);
        }
    }

    /** 解除关系 */
    public void removeTeacherFromStudent(Long teacherId, Long studentId, String subject) {
        tsRepository.deleteByTeacherIdAndStudentIdAndSubject(teacherId, studentId, subject);
    }

    /** 批量保存学生-教师-科目关系 */
    @Transactional
    public void saveRelations(Long studentId, List<Long> teacherIds, List<String> subjects) {
        // 删除旧关系
        tsRepository.deleteByStudentId(studentId);
        // 保存新关系
        if (teacherIds != null && subjects != null && teacherIds.size() == subjects.size()) {
            for (int i = 0; i < teacherIds.size(); i++) {
                if (teacherIds.get(i) != null && subjects.get(i) != null && !subjects.get(i).isEmpty()) {
                    assignTeacherToStudent(teacherIds.get(i), studentId, subjects.get(i));
                }
            }
        }
    }

    /** 统计某老师负责的学生数 */
    public long countStudentsByTeacherId(Long teacherId) {
        return getStudentIdsByTeacherId(teacherId).size();
    }
}
