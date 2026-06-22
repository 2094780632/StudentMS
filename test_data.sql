-- ============================================
-- 学生管理系统 - 测试数据插入脚本
-- 数据库：student_db (MySQL)
-- 执行方式：登录 MySQL 后执行
--   mysql -u root -p student_db < test_data.sql
-- 或在 MySQL 客户端中：source test_data.sql
-- ============================================

USE student_db;

-- ============================================
-- 第一部分：新增 3 位教师用户
-- 密码均为 123456（BCrypt 加密）
-- 你需要在实际环境中通过应用注册或替换下面密码
-- 以下密码 hash 对应明文 "123456"
-- ============================================

INSERT INTO `user` (`username`, `password`, `fullname`, `role`, `identity`, `enabled`, `create_time`)
VALUES
('teacher_wang', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5Eh', '王老师', 'ROLE_USER', 'TEACHER', 1, NOW()),
('teacher_li',   '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5Eh', '李老师', 'ROLE_USER', 'TEACHER', 1, NOW()),
('teacher_zhang','$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5Eh', '张老师', 'ROLE_USER', 'TEACHER', 1, NOW());

-- ============================================
-- 第二部分：新增 2 位学生用户
-- ============================================

INSERT INTO `user` (`username`, `password`, `fullname`, `role`, `identity`, `enabled`, `create_time`)
VALUES
('student_zhao', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5Eh', '赵同学', 'ROLE_USER', 'STUDENT', 1, NOW()),
('student_qian', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5Eh', '钱同学', 'ROLE_USER', 'STUDENT', 1, NOW());

-- ============================================
-- 第三部分：插入 20 条学生档案数据
-- teacher_id 对应上面新增教师用户的 ID
-- 假设 admin id=1, user id=2, 
-- teacher_wang id=3, teacher_li id=4, teacher_zhang id=5
-- 请根据实际 user 表的 id 调整 teacher_id 值！
-- ============================================

-- 王老师的学生（teacher_id = 3）
INSERT INTO `student` (`student_id`, `name`, `gender`, `age`, `class_name`, `major`, `teacher_id`, `create_time`, `update_time`) VALUES
('2024001', '张三',   '男', 20, '软件工程1班', '软件工程',     3, NOW(), NOW()),
('2024002', '李四',   '女', 19, '软件工程1班', '软件工程',     3, NOW(), NOW()),
('2024003', '王五',   '男', 21, '软件工程2班', '软件工程',     3, NOW(), NOW()),
('2024004', '赵六',   '女', 20, '软件工程2班', '软件工程',     3, NOW(), NOW()),
('2024005', '孙七',   '男', 22, '计算机科学1班','计算机科学与技术', 3, NOW(), NOW()),
('2024006', '周八',   '女', 19, '计算机科学1班','计算机科学与技术', 3, NOW(), NOW());

-- 李老师的学生（teacher_id = 4）
INSERT INTO `student` (`student_id`, `name`, `gender`, `age`, `class_name`, `major`, `teacher_id`, `create_time`, `update_time`) VALUES
('2024007', '吴九',   '男', 20, '网络工程1班',  '网络工程',     4, NOW(), NOW()),
('2024008', '郑十',   '女', 21, '网络工程1班',  '网络工程',     4, NOW(), NOW()),
('2024009', '陈一一', '男', 19, '信息安全1班',  '信息安全',     4, NOW(), NOW()),
('2024010', '林一二', '女', 20, '信息安全1班',  '信息安全',     4, NOW(), NOW()),
('2024011', '黄一三', '男', 22, '数据科学1班',  '数据科学与大数据',4, NOW(), NOW()),
('2024012', '何一四', '女', 19, '数据科学1班',  '数据科学与大数据',4, NOW(), NOW());

-- 张老师的学生（teacher_id = 5）
INSERT INTO `student` (`student_id`, `name`, `gender`, `age`, `class_name`, `major`, `teacher_id`, `create_time`, `update_time`) VALUES
('2024013', '刘一五', '男', 20, '人工智能1班',  '人工智能',     5, NOW(), NOW()),
('2024014', '杨一六', '女', 21, '人工智能1班',  '人工智能',     5, NOW(), NOW()),
('2024015', '吕一七', '男', 19, '人工智能2班',  '人工智能',     5, NOW(), NOW()),
('2024016', '苏一八', '女', 20, '人工智能2班',  '人工智能',     5, NOW(), NOW()),
('2024017', '蒋一九', '男', 22, '软件工程3班',  '软件工程',     5, NOW(), NOW()),
('2024018', '蔡二十', '女', 19, '软件工程3班',  '软件工程',     5, NOW(), NOW());

-- 管理员直属学生（teacher_id = 1，admin）
INSERT INTO `student` (`student_id`, `name`, `gender`, `age`, `class_name`, `major`, `teacher_id`, `create_time`, `update_time`) VALUES
('2024019', '许二一', '男', 20, '计算机科学2班','计算机科学与技术', 1, NOW(), NOW()),
('2024020', '高二二', '女', 21, '计算机科学2班','计算机科学与技术', 1, NOW(), NOW());

-- ============================================
-- 验证 SQL（插入完成后执行以确认数据）
-- ============================================

-- 查看用户总数及身份分布
SELECT '--- 用户统计 ---' AS '';
SELECT identity, COUNT(*) AS cnt FROM `user` GROUP BY identity;

-- 查看学生总数及教师分布
SELECT '--- 学生统计 ---' AS '';
SELECT t.fullname AS 教师姓名, COUNT(s.id) AS 学生人数
FROM `student` s LEFT JOIN `user` t ON s.teacher_id = t.id
GROUP BY s.teacher_id, t.fullname
ORDER BY 学生人数 DESC;

-- 查看所有学生
SELECT '--- 全部学生 ---' AS '';
SELECT s.student_id AS 学号, s.name AS 姓名, s.gender AS 性别,
       s.age AS 年龄, s.class_name AS 班级, s.major AS 专业,
       COALESCE(u.fullname, '未分配') AS 负责教师
FROM `student` s
LEFT JOIN `user` u ON s.teacher_id = u.id
ORDER BY s.teacher_id, s.student_id;
