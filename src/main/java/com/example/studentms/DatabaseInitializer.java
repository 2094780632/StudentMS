package com.example.studentms;

import com.example.studentms.entity.User;
import com.example.studentms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("123456");
            admin.setFullname("系统管理员");
            admin.setRole("ROLE_ADMIN");
            admin.setIdentity("TEACHER");
            admin.setEnabled(true);
            userRepository.save(admin);

            User normal = new User();
            normal.setUsername("user");
            normal.setPassword("123456");
            normal.setFullname("普通用户");
            normal.setRole("ROLE_USER");
            normal.setIdentity("STUDENT");
            normal.setEnabled(true);
            userRepository.save(normal);

            System.out.println("=========================================");
            System.out.println("初始化用户创建成功！");
            System.out.println("管理员账号: admin / 123456");
            System.out.println("普通账号:   user / 123456");
            System.out.println("=========================================");
        }
    }
}