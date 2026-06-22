package com.example.studentms;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.studentms.entity.Student;
import com.example.studentms.entity.User;
import com.example.studentms.repository.StudentRepository;
import com.example.studentms.repository.UserRepository;

@Component
@Order(2)
public class TestDataInitializer implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        // 仅在用户表中只有初始 2 条数据时插入（admin + user）
        if (userRepository.count() > 2) {
            System.out.println("[测试数据] 用户已超过2个，跳过插入");
            return;
        }
        if (studentRepository.count() > 0) {
            System.out.println("[测试数据] 学生数据已存在，跳过插入");
            return;
        }

        System.out.println("========== 测试数据插入开始 ==========");

        // 1. 插入 3 位教师用户
        User t1 = createUser("teacher_wang", "123456", "王老师", "ROLE_USER", "TEACHER");
        User t2 = createUser("teacher_li", "123456", "李老师", "ROLE_USER", "TEACHER");
        User t3 = createUser("teacher_zhang", "123456", "张老师", "ROLE_USER", "TEACHER");

        // 2. 插入 2 位学生用户
        createUser("student_zhao", "123456", "赵同学", "ROLE_USER", "STUDENT");
        createUser("student_qian", "123456", "钱同学", "ROLE_USER", "STUDENT");

        // 3. 查找 admin 的 id
        User admin = userRepository.findByUsername("admin").orElse(null);
        Long adminId = admin != null ? admin.getId() : null;

        // 4. 插入学生档案
        int count = 0;
        LocalDateTime now = LocalDateTime.now();

        // 王老师的学生 (6 条)
        count += createStudent("2024001", "张三", "男", 20, "软件工程1班", "软件工程", t1.getId(), now);
        count += createStudent("2024002", "李四", "女", 19, "软件工程1班", "软件工程", t1.getId(), now);
        count += createStudent("2024003", "王五", "男", 21, "软件工程2班", "软件工程", t1.getId(), now);
        count += createStudent("2024004", "赵六", "女", 20, "软件工程2班", "软件工程", t1.getId(), now);
        count += createStudent("2024005", "孙七", "男", 22, "计算机科学1班", "计算机科学与技术", t1.getId(), now);
        count += createStudent("2024006", "周八", "女", 19, "计算机科学1班", "计算机科学与技术", t1.getId(), now);

        // 李老师的学生 (6 条)
        count += createStudent("2024007", "吴九", "男", 20, "网络工程1班", "网络工程", t2.getId(), now);
        count += createStudent("2024008", "郑十", "女", 21, "网络工程1班", "网络工程", t2.getId(), now);
        count += createStudent("2024009", "陈一一", "男", 19, "信息安全1班", "信息安全", t2.getId(), now);
        count += createStudent("2024010", "林一二", "女", 20, "信息安全1班", "信息安全", t2.getId(), now);
        count += createStudent("2024011", "黄一三", "男", 22, "数据科学1班", "数据科学与大数据", t2.getId(), now);
        count += createStudent("2024012", "何一四", "女", 19, "数据科学1班", "数据科学与大数据", t2.getId(), now);

        // 张老师的学生 (6 条)
        count += createStudent("2024013", "刘一五", "男", 20, "人工智能1班", "人工智能", t3.getId(), now);
        count += createStudent("2024014", "杨一六", "女", 21, "人工智能1班", "人工智能", t3.getId(), now);
        count += createStudent("2024015", "吕一七", "男", 19, "人工智能2班", "人工智能", t3.getId(), now);
        count += createStudent("2024016", "苏一八", "女", 20, "人工智能2班", "人工智能", t3.getId(), now);
        count += createStudent("2024017", "蒋一九", "男", 22, "软件工程3班", "软件工程", t3.getId(), now);
        count += createStudent("2024018", "蔡二十", "女", 19, "软件工程3班", "软件工程", t3.getId(), now);

        // 管理员直属学生 (2 条)
        if (adminId != null) {
            count += createStudent("2024019", "许二一", "男", 20, "计算机科学2班", "计算机科学与技术", adminId, now);
            count += createStudent("2024020", "高二二", "女", 21, "计算机科学2班", "计算机科学与技术", adminId, now);
        }

        System.out.println("========== 测试数据插入完成 ==========");
        System.out.println("新增用户: 5 人 (3 教师 + 2 学生)");
        System.out.println("新增学生档案: " + count + " 条");
        System.out.println("教师账号: teacher_wang / teacher_li / teacher_zhang");
        System.out.println("学生账号: student_zhao / student_qian");
        System.out.println("密码统一: 123456");
        System.out.println("=========================================");
    }

    private User createUser(String username, String rawPassword, String fullname, String role, String identity) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setFullname(fullname);
        user.setRole(role);
        user.setIdentity(identity);
        user.setEnabled(true);
        return userRepository.save(user);
    }

    private int createStudent(String studentId, String name, String gender, int age,
                              String className, String major, Long teacherId, LocalDateTime now) {
        Student s = new Student();
        s.setStudentId(studentId);
        s.setName(name);
        s.setGender(gender);
        s.setAge(age);
        s.setClassName(className);
        s.setMajor(major);
        s.setTeacherId(teacherId);
        s.setCreateTime(now);
        s.setUpdateTime(now);
        studentRepository.save(s);
        return 1;
    }
}
