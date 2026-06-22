-- ============================================
-- 学生管理系统 - 数据库重置 + 测试数据
-- 数据库：student_db (MySQL)
-- 
-- 执行方式：
--   方法1: mysql -u root -p < reset_and_seed.sql
--   方法2: 登录 MySQL 后 source D:/Project/WEB/StudentMS/reset_and_seed.sql
--
-- 注意：此脚本会删除所有表并重建，请确认无重要数据！
--       所有用户密码均哈希自明文 "123456"
--       哈希值由 BCryptPasswordEncoder 生成
-- ============================================

-- 使用 student_db（如果不存在则创建）
CREATE DATABASE IF NOT EXISTS student_db DEFAULT CHARACTER SET utf8mb4;
USE student_db;

-- ============================================
-- 第一步：删除所有表（CASCADE 顺序）
-- ============================================
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `teacher_student`;
DROP TABLE IF EXISTS `log`;
DROP TABLE IF EXISTS `student`;
DROP TABLE IF EXISTS `user`;
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- 第二步：创建表（与 JPA ddl-auto=update 一致）
-- ============================================

CREATE TABLE `user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(255) NOT NULL UNIQUE,
    `password` VARCHAR(255) DEFAULT NULL,
    `fullname` VARCHAR(255) DEFAULT NULL,
    `role` VARCHAR(255) DEFAULT NULL,
    `identity` VARCHAR(20) DEFAULT 'TEACHER',
    `enabled` BIT(1) DEFAULT 1,
    `create_time` DATETIME(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `student` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `student_id` VARCHAR(255) DEFAULT NULL UNIQUE,
    `name` VARCHAR(255) DEFAULT NULL,
    `gender` VARCHAR(255) DEFAULT NULL,
    `age` INT DEFAULT NULL,
    `class_name` VARCHAR(255) DEFAULT NULL,
    `major` VARCHAR(255) DEFAULT NULL,
    `grade` VARCHAR(255) DEFAULT NULL,
    `create_time` DATETIME(6) DEFAULT NULL,
    `update_time` DATETIME(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `log` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(255) DEFAULT NULL,
    `operation` VARCHAR(255) DEFAULT NULL,
    `detail` VARCHAR(500) DEFAULT NULL,
    `ip` VARCHAR(255) DEFAULT NULL,
    `create_time` DATETIME(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `teacher_student` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `teacher_id` BIGINT DEFAULT NULL,
    `student_id` BIGINT DEFAULT NULL,
    `subject` VARCHAR(255) DEFAULT NULL,
    UNIQUE KEY `uk_teacher_student_subject` (`teacher_id`, `student_id`, `subject`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- 第三步：插入用户（密码均为明文 123456）
-- 使用 NoOpPasswordEncoder，无需加密
-- ============================================

-- 管理员
INSERT INTO `user` (`username`, `password`, `fullname`, `role`, `identity`, `enabled`, `create_time`) VALUES
('admin', '123456', '系统管理员', 'ROLE_ADMIN', 'TEACHER', 1, NOW());

-- 教师（4人）
INSERT INTO `user` (`username`, `password`, `fullname`, `role`, `identity`, `enabled`, `create_time`) VALUES
('teacher_wang',  '123456', '王老师', 'ROLE_USER', 'TEACHER', 1, NOW()),
('teacher_li',    '123456', '李老师', 'ROLE_USER', 'TEACHER', 1, NOW()),
('teacher_zhang', '123456', '张老师', 'ROLE_USER', 'TEACHER', 1, NOW()),
('teacher_zhao',  '123456', '赵老师', 'ROLE_USER', 'TEACHER', 1, NOW());

-- 学生用户（4人）
INSERT INTO `user` (`username`, `password`, `fullname`, `role`, `identity`, `enabled`, `create_time`) VALUES
('student_zhao', '123456', '赵同学', 'ROLE_USER', 'STUDENT', 1, NOW()),
('student_qian', '123456', '钱同学', 'ROLE_USER', 'STUDENT', 1, NOW()),
('student_sun',  '123456', '孙同学', 'ROLE_USER', 'STUDENT', 1, NOW()),
('student_zhou', '123456', '周同学', 'ROLE_USER', 'STUDENT', 1, NOW());

-- ============================================
-- 第四步：插入学生档案（30条）
-- admin=1, teacher_wang=2, teacher_li=3, teacher_zhang=4, teacher_zhao=5
-- student_zhao=6, student_qian=7, student_sun=8, student_zhou=9
-- ============================================

-- 高三（10人）
INSERT INTO `student` (`student_id`, `name`, `gender`, `age`, `class_name`, `major`, `grade`, `create_time`, `update_time`) VALUES
('S20240001', '张伟',   '男', 18, '高三1班', '理科综合',  '高三', NOW(), NOW()),
('S20240002', '李娜',   '女', 17, '高三1班', '理科综合',  '高三', NOW(), NOW()),
('S20240003', '王磊',   '男', 18, '高三2班', '理科综合',  '高三', NOW(), NOW()),
('S20240004', '刘洋',   '女', 17, '高三2班', '文科综合',  '高三', NOW(), NOW()),
('S20240005', '陈静',   '女', 18, '高三3班', '文科综合',  '高三', NOW(), NOW()),
('S20240006', '杨帆',   '男', 18, '高三3班', '理科综合',  '高三', NOW(), NOW()),
('S20240007', '赵敏',   '女', 17, '高三4班', '文科综合',  '高三', NOW(), NOW()),
('S20240008', '黄强',   '男', 18, '高三4班', '理科综合',  '高三', NOW(), NOW()),
('S20240009', '周杰',   '男', 17, '高三5班', '理科综合',  '高三', NOW(), NOW()),
('S20240010', '吴欣',   '女', 18, '高三5班', '文科综合',  '高三', NOW(), NOW());

-- 高二（10人）
INSERT INTO `student` (`student_id`, `name`, `gender`, `age`, `class_name`, `major`, `grade`, `create_time`, `update_time`) VALUES
('S20230001', '郑爽',   '女', 17, '高二1班', '理科综合',  '高二', NOW(), NOW()),
('S20230002', '孙鹏',   '男', 16, '高二1班', '理科综合',  '高二', NOW(), NOW()),
('S20230003', '马丽',   '女', 17, '高二2班', '文科综合',  '高二', NOW(), NOW()),
('S20230004', '朱峰',   '男', 16, '高二2班', '理科综合',  '高二', NOW(), NOW()),
('S20230005', '胡涛',   '男', 17, '高二3班', '文科综合',  '高二', NOW(), NOW()),
('S20230006', '林黛',   '女', 16, '高二3班', '理科综合',  '高二', NOW(), NOW()),
('S20230007', '何平',   '男', 17, '高二4班', '理科综合',  '高二', NOW(), NOW()),
('S20230008', '郭靖',   '男', 16, '高二4班', '文科综合',  '高二', NOW(), NOW()),
('S20230009', '罗薇',   '女', 17, '高二5班', '文科综合',  '高二', NOW(), NOW()),
('S20230010', '梁宇',   '男', 16, '高二5班', '理科综合',  '高二', NOW(), NOW());

-- 高一（10人）
INSERT INTO `student` (`student_id`, `name`, `gender`, `age`, `class_name`, `major`, `grade`, `create_time`, `update_time`) VALUES
('S20220001', '宋明',   '男', 16, '高一1班', '理科综合',  '高一', NOW(), NOW()),
('S20220002', '唐琪',   '女', 15, '高一1班', '文科综合',  '高一', NOW(), NOW()),
('S20220003', '韩磊',   '男', 16, '高一2班', '理科综合',  '高一', NOW(), NOW()),
('S20220004', '冯琳',   '女', 15, '高一2班', '文科综合',  '高一', NOW(), NOW()),
('S20220005', '曹阳',   '男', 16, '高一3班', '理科综合',  '高一', NOW(), NOW()),
('S20220006', '许慧',   '女', 15, '高一3班', '文科综合',  '高一', NOW(), NOW()),
('S20220007', '邓超',   '男', 16, '高一4班', '理科综合',  '高一', NOW(), NOW()),
('S20220008', '彭丹',   '女', 15, '高一4班', '文科综合',  '高一', NOW(), NOW()),
('S20220009', '萧剑',   '男', 16, '高一5班', '理科综合',  '高一', NOW(), NOW()),
('S20220010', '秦瑶',   '女', 15, '高一5班', '文科综合',  '高一', NOW(), NOW());

-- ============================================
-- 第五步：插入师生科目关系（teacher_student）
-- student表id: 1-10(高三), 11-20(高二), 21-30(高一)
-- user表id: admin=1, wang=2, li=3, zhang=4, zhao=5
-- ============================================

-- 王老师(2)：高三1-2班 数学+物理（学生id 1-4）
INSERT INTO `teacher_student` (`teacher_id`, `student_id`, `subject`) VALUES
(2, 1, '数学'), (2, 1, '物理'),
(2, 2, '数学'), (2, 2, '物理'),
(2, 3, '数学'), (2, 3, '物理'),
(2, 4, '数学'), (2, 4, '物理');

-- 李老师(3)：高三3-5班 语文+英语（学生id 5-10）
INSERT INTO `teacher_student` (`teacher_id`, `student_id`, `subject`) VALUES
(3, 5, '语文'), (3, 5, '英语'),
(3, 6, '语文'), (3, 6, '英语'),
(3, 7, '语文'), (3, 7, '英语'),
(3, 8, '语文'), (3, 8, '英语'),
(3, 9, '语文'), (3, 9, '英语'),
(3, 10, '语文'), (3, 10, '英语');

-- 张老师(4)：高二全部 化学+生物（学生id 11-20）
INSERT INTO `teacher_student` (`teacher_id`, `student_id`, `subject`) VALUES
(4, 11, '化学'), (4, 11, '生物'),
(4, 12, '化学'), (4, 12, '生物'),
(4, 13, '化学'), (4, 13, '生物'),
(4, 14, '化学'), (4, 14, '生物'),
(4, 15, '化学'), (4, 15, '生物'),
(4, 16, '化学'), (4, 16, '生物'),
(4, 17, '化学'), (4, 17, '生物'),
(4, 18, '化学'), (4, 18, '生物'),
(4, 19, '化学'), (4, 19, '生物'),
(4, 20, '化学'), (4, 20, '生物');

-- 赵老师(5)：高一全部 历史+地理+政治（学生id 21-30）
INSERT INTO `teacher_student` (`teacher_id`, `student_id`, `subject`) VALUES
(5, 21, '历史'), (5, 21, '地理'), (5, 21, '政治'),
(5, 22, '历史'), (5, 22, '地理'), (5, 22, '政治'),
(5, 23, '历史'), (5, 23, '地理'), (5, 23, '政治'),
(5, 24, '历史'), (5, 24, '地理'), (5, 24, '政治'),
(5, 25, '历史'), (5, 25, '地理'), (5, 25, '政治'),
(5, 26, '历史'), (5, 26, '地理'), (5, 26, '政治'),
(5, 27, '历史'), (5, 27, '地理'), (5, 27, '政治'),
(5, 28, '历史'), (5, 28, '地理'), (5, 28, '政治'),
(5, 29, '历史'), (5, 29, '地理'), (5, 29, '政治'),
(5, 30, '历史'), (5, 30, '地理'), (5, 30, '政治');

-- 管理员(1)：兼任高三1-2班 班主任（数学）（学生id 1-4）
INSERT INTO `teacher_student` (`teacher_id`, `student_id`, `subject`) VALUES
(1, 1, '数学'),
(1, 2, '数学'),
(1, 3, '数学'),
(1, 4, '数学');

-- ============================================
-- 第六步：验证数据
-- ============================================

SELECT '' AS '';
SELECT '==================== 用户列表 ====================' AS '';
SELECT id, username, fullname, role, identity,
       CASE WHEN enabled = 1 THEN '启用' ELSE '停用' END AS status
FROM `user` ORDER BY id;

SELECT '' AS '';
SELECT '==================== 身份统计 ====================' AS '';
SELECT identity, COUNT(*) AS cnt FROM `user` GROUP BY identity;

SELECT '' AS '';
SELECT '==================== 学生统计（按年级） ====================' AS '';
SELECT grade AS 年级, COUNT(*) AS 学生人数 FROM `student` GROUP BY grade ORDER BY grade;

SELECT '' AS '';
SELECT '==================== 每个老师负责的学生数 ====================' AS '';
SELECT u.fullname AS 教师, COUNT(DISTINCT ts.student_id) AS 学生数,
       GROUP_CONCAT(DISTINCT ts.subject ORDER BY ts.subject) AS 教授科目
FROM `teacher_student` ts
JOIN `user` u ON ts.teacher_id = u.id
GROUP BY ts.teacher_id, u.fullname
ORDER BY 学生数 DESC;

SELECT '' AS '';
SELECT '==================== 账号速查表 ====================' AS '';
SELECT username AS 账号, '123456' AS 密码, role AS 角色, identity AS 身份 FROM `user` ORDER BY id;

SELECT '' AS '';
SELECT '==================== 数据库重置完成！ ====================' AS '';
