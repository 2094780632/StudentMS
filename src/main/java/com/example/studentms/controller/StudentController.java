package com.example.studentms.controller;

import com.example.studentms.annotation.LogAnnotation;
import com.example.studentms.entity.Student;
import com.example.studentms.service.StudentService;
import com.example.studentms.service.TeacherStudentService;
import com.example.studentms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    @Autowired
    private TeacherStudentService teacherStudentService;

    // 学生列表页（支持分页和搜索）
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(required = false) String keyword,
                       Model model) {
        Page<Student> studentPage = studentService.searchStudents(keyword, page - 1, 10);
        model.addAttribute("students", studentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("teacherMap", userService.getTeacherNameMap());
        model.addAttribute("tsService", teacherStudentService);
        return "student-list";
    }

    // 跳转到新增表单
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("teacherList", userService.findTeachers());
        model.addAttribute("subjects", TeacherStudentService.SUBJECTS);
        return "student-form";
    }

    // 保存（新增或更新）
    @PostMapping("/save")
    @LogAnnotation(operation = "新增/编辑学生", detail = "#student.name + '(' + #student.studentId + ')'")
    public String save(@ModelAttribute Student student,
                       @RequestParam(required = false) List<Long> teacherIds,
                       @RequestParam(required = false) List<String> subjectNames) {
        studentService.save(student);
        // 保存科任老师关系（管理员分配）
        if (teacherIds != null && subjectNames != null) {
            teacherStudentService.saveRelations(student.getId(), teacherIds, subjectNames);
        }
        return "redirect:/student/list";
    }

    // 跳转到编辑表单
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Student student = studentService.findById(id);
        if (student == null) {
            return "redirect:/student/list";
        }
        model.addAttribute("student", student);
        model.addAttribute("teacherList", userService.findTeachers());
        model.addAttribute("subjects", TeacherStudentService.SUBJECTS);
        // 已有科任老师关系
        model.addAttribute("existingRelations", teacherStudentService.getRelationsByStudentId(id));
        return "student-form";
    }

    // 删除学生
    @GetMapping("/delete/{id}")
    @LogAnnotation(operation = "删除学生", detail = "ID: " + "#id")
    public String delete(@PathVariable Long id) {
        studentService.deleteById(id);
        return "redirect:/student/list";
    }
}