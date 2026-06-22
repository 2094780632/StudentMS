package com.example.studentms.advice;

import com.example.studentms.entity.User;
import com.example.studentms.util.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAdvice {

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @ModelAttribute("currentUser")
    public User addCurrentUser() {
        return currentUserUtil.getCurrentUser();
    }
}
