package com.example.studentms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.studentms.entity.User;
import com.example.studentms.service.LogService;
import com.example.studentms.service.StudentService;
import com.example.studentms.service.UserService;

@Controller
public class AuthController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        User currentUser = userService.getCurrentUser();
        long studentCount;
        if (currentUser != null && "TEACHER".equals(currentUser.getIdentity())
                && !"ROLE_ADMIN".equals(currentUser.getRole())) {
            // 教师：只统计自己名下的学生
            studentCount = studentService.countByTeacherId(currentUser.getId());
        } else {
            // 管理员及其他人：统计所有
            studentCount = studentService.countAll();
        }
        model.addAttribute("studentCount", studentCount);
        model.addAttribute("userCount", userService.countAll());
        model.addAttribute("teacherCount", userService.countByIdentity("TEACHER"));
        model.addAttribute("studentUserCount", userService.countByIdentity("STUDENT"));
        model.addAttribute("logTodayCount", logService.countToday());
        model.addAttribute("recentLogs", logService.findLatest(5));
        return "dashboard";
    }
}