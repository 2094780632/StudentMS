package com.example.studentms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.studentms.entity.User;
import com.example.studentms.repository.UserRepository;

@Component
@Order(1)
public class DataCorrectionRunner implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("========== 数据库校正开始 ==========");

        // 1. 校正 identity 为空的用户：根据 role 推断身份
        for (User user : userRepository.findAll()) {
            boolean updated = false;

            if (user.getIdentity() == null || user.getIdentity().isEmpty()) {
                if ("ROLE_ADMIN".equals(user.getRole())) {
                    user.setIdentity("TEACHER");
                } else {
                    user.setIdentity("STUDENT");
                }
                updated = true;
            }

            if (updated) {
                userRepository.save(user);
                System.out.println("校正用户 [" + user.getUsername() + "]: identity -> " + user.getIdentity());
            }
        }

        System.out.println("========== 数据库校正完成 ==========");
    }
}
