package com.example.studentms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.studentms.annotation.LogAnnotation;
import com.example.studentms.entity.User;
import com.example.studentms.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 用户列表页（支持分页和搜索）
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(required = false) String keyword,
                       Model model) {
        Page<User> userPage = userService.findUsers(keyword, page - 1, 10);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        return "user-list";
    }

    // 跳转到新增表单
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("user", new User());
        return "user-form";
    }

    // 跳转到编辑表单
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "user-form";
    }

    // 保存（新增或更新）
    @PostMapping("/save")
    @LogAnnotation(operation = "新增/编辑用户")
    public String save(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/user/list";
    }

    // 删除用户
    @GetMapping("/delete/{id}")
    @LogAnnotation(operation = "删除用户")
    public String delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/user/list";
    }
}
