# 学生管理系统 (StudentMS) — 功能总览

> **技术栈**：Spring Boot 3.5.15 + Spring Security + JPA + Thymeleaf + Bootstrap 5 + Bootstrap Icons + Apache POI  
> **数据库**：MySQL 8.x (`student_db`)，JPA `ddl-auto=update`  
> **开发环境**：JDK 17 + Maven + PowerShell

---

## 一、用户体系（三层身份）

| 层级 | 字段 | 说明 |
|---|---|---|
| 角色 | `role` | `ROLE_ADMIN` 管理员 / `ROLE_USER` 普通用户，控制权限 |
| 身份 | `identity` | `TEACHER` 老师 / `STUDENT` 学生，控制数据可见性和菜单 |

**权限规则**：
- `/user/**`、`/log/**` → 仅 `ROLE_ADMIN`
- `/teacher-student/**` → 需登录
- 其余路径 → 需登录

---

## 二、功能模块

### 1. 登录与会话管理

| 功能 | 路由 | 说明 |
|---|---|---|
| 登录页 | `GET /login` | 自定义样式登录页面 |
| 登出 | `/logout` | 退出后跳转 `/login?logout` |
| 默认跳转 | - | 登录成功 → `/dashboard` |

### 2. 控制面板 (Dashboard)

| 路由 | 访问身份 | 展示内容 |
|---|---|---|
| `GET /dashboard` | 管理员 | 学生总数、用户总数、教师人数、学生人数、今日操作、快速入口、最近操作动态表格 |
| | 教师 | 我负责的学生数、全校学生总数、系统用户总数、快速入口、欢迎语 |
| | 学生 | 我的科任老师数、全校学生总数、全校教师人数、科任老师列表（姓名+科目）、快速入口、欢迎语 |

### 3. 学生管理

| 功能 | 路由 | 权限说明 |
|---|---|---|
| 学生列表 | `GET /student/list?page=&keyword=` | 管理员→全部；教师→仅自己科目下的学生；学生→空 |
| 新增学生 | `GET /student/add` | 教师/管理员 |
| 编辑学生 | `GET /student/edit/{id}` | 仅自己有权限的学生 |
| 保存学生 | `POST /student/save` | 管理员可分配多科任老师+科目 |
| 删除学生 | `GET /student/delete/{id}` | 仅自己有权限的学生 |
| 导出 Excel | `GET /export/students?keyword=` | 数据隔离同列表页，生成 `.xlsx` |

**学生实体字段**：学号、姓名、性别、年龄、班级、专业、年级

### 4. 用户管理（仅管理员）

| 功能 | 路由 | 说明 |
|---|---|---|
| 用户列表 | `GET /user/list?page=&keyword=` | 按用户名搜索，分页，含身份徽章 |
| 新增用户 | `GET /user/add` | 表单：用户名、姓名、密码、角色、身份、启用状态 |
| 编辑用户 | `GET /user/edit/{id}` | 用户名只读，密码留空不修改 |
| 保存用户 | `POST /user/save` | 新增时密码明文写入，编辑时空密码回填原值 |
| 删除用户 | `GET /user/delete/{id}` | 确认弹窗 |

**用户实体字段**：用户名、密码、真实姓名、角色（ROLE_ADMIN/ROLE_USER）、身份（TEACHER/STUDENT）、启用状态

### 5. 操作日志（仅管理员）

| 功能 | 路由 | 说明 |
|---|---|---|
| 日志列表 | `GET /log/list?page=&username=` | 按操作人筛选，分页 |
| 日志记录 | 自动（AOP） | `@LogAnnotation` 注解自动记录：操作人、操作类型、详情、IP、时间 |

### 6. 多对多科任老师制

| 功能 | 路由 | 说明 |
|---|---|---|
| 我的学生（教师） | `GET /teacher-student/my-students` | 教师查看自己负责的学生及科目 |
| 我的老师（学生） | `GET /teacher-student/my-teachers` | 学生查看自己所有科任老师及科目 |
| 分配关系 | POST `/student/save` | 管理员编辑学生时可动态添加/删除（老师+科目）配对 |

**中间表** `teacher_student`：`teacher_id` + `student_id` + `subject`（唯一约束）

**预设科目（9个）**：数学、语文、英语、物理、化学、生物、历史、地理、政治

---

## 三、前端页面（10个模板）

| 模板文件 | 对应功能 |
|---|---|
| `login.html` | 登录页面 |
| `dashboard.html` | 控制面板（按角色三段式布局） |
| `student-list.html` | 学生列表（含科任老师列、搜索、分页、导出按钮） |
| `student-form.html` | 学生表单（含年级下拉、科任老师动态配对 UI） |
| `user-list.html` | 用户列表（身份/角色徽章） |
| `user-form.html` | 用户表单（含身份下拉） |
| `log-list.html` | 操作日志列表 |
| `my-students.html` | 教师-我的学生页 |
| `my-teachers.html` | 学生-我的老师页 |
| `fragments/navbar.html` | 公共导航栏（按身份动态显示菜单） |

---

## 四、技术特性

| 特性 | 实现方式 |
|---|---|
| 数据隔离 | Service 层根据 `CurrentUserUtil` 判断角色/身份，管理员查所有，教师查自己学生 |
| 操作审计 | 自定义 `@LogAnnotation` + AOP 切面，自动记录增删改查操作 |
| 全局模型注入 | `@ControllerAdvice` → `GlobalModelAdvice` 全局注入 `currentUser` |
| 导航栏动态化 | `th:if="${currentUser.identity == 'TEACHER'}"` 按身份显隐菜单 |
| Excel 导出 | Apache POI `XSSFWorkbook`，自动继承数据隔离 |
| 密码策略 | 测试阶段使用 `NoOpPasswordEncoder` 明文存储（生产需改回 BCrypt） |
| Bootstrap 5 | CDN 引入，响应式布局，Bootstrap Icons 图标 |

---

## 五、项目结构

```
src/main/java/com/example/studentms/
├── StudentMsApplication.java
├── DatabaseInitializer.java        # 首次启动建 admin/user 账号
├── DataCorrectionRunner.java       # 校正旧数据 identity 字段
├── advice/
│   └── GlobalModelAdvice.java      # 全局注入 currentUser
├── annotation/
│   └── LogAnnotation.java          # 操作日志注解
├── aspect/
│   └── LogAspect.java              # 操作日志 AOP
├── config/
│   └── SecurityConfig.java         # Spring Security 配置
├── controller/
│   ├── AuthController.java         # /login, /dashboard
│   ├── StudentController.java      # /student/*
│   ├── UserController.java         # /user/*
│   ├── ExportController.java       # /export/students
│   ├── LogController.java          # /log/list
│   └── TeacherStudentController.java # /teacher-student/*
├── entity/
│   ├── Student.java
│   ├── User.java
│   ├── Log.java
│   └── TeacherStudent.java
├── repository/
│   ├── StudentRepository.java
│   ├── UserRepository.java
│   ├── LogRepository.java
│   └── TeacherStudentRepository.java
├── service/
│   ├── StudentService.java
│   ├── UserService.java
│   ├── LogService.java
│   ├── ExportService.java
│   ├── TeacherStudentService.java
│   └── CustomUserDetailsService.java
└── util/
    ├── CurrentUserUtil.java        # SecurityContext 工具类
    └── IPUtil.java                 # IP 获取工具

src/main/resources/
├── application.properties
└── templates/
    ├── login.html
    ├── dashboard.html
    ├── student-list.html
    ├── student-form.html
    ├── user-list.html
    ├── user-form.html
    ├── log-list.html
    ├── my-students.html
    ├── my-teachers.html
    └── fragments/
        └── navbar.html
```

---

## 六、测试账号

| 账号 | 密码 | 角色 | 身份 |
|---|---|---|---|
| `admin` | `123456` | ROLE_ADMIN | TEACHER |
| `teacher_wang` | `123456` | ROLE_USER | TEACHER |
| `teacher_li` | `123456` | ROLE_USER | TEACHER |
| `teacher_zhang` | `123456` | ROLE_USER | TEACHER |
| `teacher_zhao` | `123456` | ROLE_USER | TEACHER |
| `student_zhao` | `123456` | ROLE_USER | STUDENT |
| `student_qian` | `123456` | ROLE_USER | STUDENT |
| `student_sun` | `123456` | ROLE_USER | STUDENT |
| `student_zhou` | `123456` | ROLE_USER | STUDENT |

> 执行 `reset_and_seed.sql` 可重置数据库并导入 30 条学生 + 完整师生关系。

---

## 七、启动方式

```bash
# 编译并启动
.\mvnw.cmd spring-boot:run

# 或先编译再运行 JAR
.\mvnw.cmd package -DskipTests
java -jar target\StudentMS-0.0.1-SNAPSHOT.jar
```

访问 `http://localhost:8080` → 自动跳转登录页。
