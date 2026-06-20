package com.example.studentms.controller;

import com.example.studentms.annotation.LogAnnotation;
import com.example.studentms.entity.Student;
import com.example.studentms.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // 学生列表页（支持分页和搜索）
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(required = false) String keyword,
                       Model model) {
        // 注意：page从1开始，但Pageable从0开始
        Page<Student> studentPage = studentService.searchStudents(keyword, page - 1, 10);
        model.addAttribute("students", studentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        return "student-list";
    }

    // 跳转到新增表单
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("student", new Student());
        return "student-form";
    }

    // 保存（新增或更新）
    @PostMapping("/save")
    @LogAnnotation(operation = "新增/编辑学生", detail = "#student.name + '(' + #student.studentId + ')'")
    public String save(@ModelAttribute Student student) {
        studentService.save(student);
        return "redirect:/student/list";
    }

    // 跳转到编辑表单
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Student student = studentService.findById(id);
        model.addAttribute("student", student);
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