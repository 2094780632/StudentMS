package com.example.studentms.controller;

import com.example.studentms.entity.Student;
import com.example.studentms.entity.User;
import com.example.studentms.service.TeacherStudentService;
import com.example.studentms.service.UserService;
import com.example.studentms.util.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/teacher-student")
public class TeacherStudentController {

    @Autowired
    private TeacherStudentService teacherStudentService;

    @Autowired
    private UserService userService;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    /** 教师查看自己负责的学生 */
    @GetMapping("/my-students")
    public String myStudents(Model model) {
        User currentUser = currentUserUtil.getCurrentUser();
        if (currentUser == null || !currentUserUtil.isTeacher()) {
            return "redirect:/dashboard";
        }
        List<Student> students = teacherStudentService.getStudentsByTeacherId(currentUser.getId());
        // 为每个学生获取科目信息
        model.addAttribute("students", students);
        model.addAttribute("tsService", teacherStudentService);
        model.addAttribute("teacherMap", userService.getTeacherNameMap());
        return "my-students";
    }

    /** 学生查看自己的科任老师 */
    @GetMapping("/my-teachers")
    public String myTeachers(Model model) {
        User currentUser = currentUserUtil.getCurrentUser();
        if (currentUser == null || !currentUserUtil.isStudent()) {
            return "redirect:/dashboard";
        }
        List<Map<String, String>> details = teacherStudentService.getTeacherSubjectDetails(currentUser.getId());
        model.addAttribute("teacherDetails", details);
        return "my-teachers";
    }
}
