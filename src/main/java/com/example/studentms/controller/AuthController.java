package com.example.studentms.controller;

import com.example.studentms.entity.User;
import com.example.studentms.service.LogService;
import com.example.studentms.service.StudentService;
import com.example.studentms.service.TeacherStudentService;
import com.example.studentms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class AuthController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

    @Autowired
    private TeacherStudentService teacherStudentService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        User currentUser = userService.getCurrentUser();

        // 管理员数据
        model.addAttribute("studentCount", studentService.countAll());
        model.addAttribute("userCount", userService.countAll());
        model.addAttribute("teacherCount", userService.countByIdentity("TEACHER"));
        model.addAttribute("studentUserCount", userService.countByIdentity("STUDENT"));
        model.addAttribute("logTodayCount", logService.countToday());
        model.addAttribute("recentLogs", logService.findLatest(5));

        // 教师数据
        if (currentUser != null && "TEACHER".equals(currentUser.getIdentity())) {
            long myStudentCount = teacherStudentService.countStudentsByTeacherId(currentUser.getId());
            model.addAttribute("myStudentCount", myStudentCount);
        }

        // 学生数据
        if (currentUser != null && "STUDENT".equals(currentUser.getIdentity())) {
            List<Map<String, String>> teacherDetails = teacherStudentService.getTeacherSubjectDetails(currentUser.getId());
            model.addAttribute("myTeacherCount", teacherDetails.size());
            model.addAttribute("myTeachers", teacherDetails);
        }

        return "dashboard";
    }
}