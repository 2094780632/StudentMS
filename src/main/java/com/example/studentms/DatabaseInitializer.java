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
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullname("系统管理员");
            admin.setRole("ROLE_ADMIN");
            admin.setEnabled(true);
            userRepository.save(admin);

            User normal = new User();
            normal.setUsername("user");
            normal.setPassword(passwordEncoder.encode("user123"));
            normal.setFullname("普通用户");
            normal.setRole("ROLE_USER");
            normal.setEnabled(true);
            userRepository.save(normal);

            System.out.println("=========================================");
            System.out.println("初始化用户创建成功！");
            System.out.println("管理员账号: admin / admin123");
            System.out.println("普通账号:   user / user123");
            System.out.println("=========================================");
        }
    }
}