package com.example.studentms.aspect;

import com.example.studentms.annotation.LogAnnotation;
import com.example.studentms.entity.Log;
import com.example.studentms.entity.Student;
import com.example.studentms.service.LogService;
import com.example.studentms.util.IPUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class LogAspect {

    @Autowired
    private LogService logService;

    // 切点：所有标注了 @LogAnnotation 的方法
    @Pointcut("@annotation(com.example.studentms.annotation.LogAnnotation)")
    public void logPointCut() {}

    // 方法执行成功后记录日志
    @AfterReturning(pointcut = "logPointCut()", returning = "result")
    public void saveLog(JoinPoint joinPoint, Object result) {
        try {
            // 1. 获取当前 HTTP 请求
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) return;
            HttpServletRequest request = attributes.getRequest();

            // 2. 获取当前登录用户名
            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            // 3. 获取客户端 IP
            String ip = IPUtil.getIpAddress(request);

            // 4. 获取方法上的注解信息
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            LogAnnotation annotation = signature.getMethod().getAnnotation(LogAnnotation.class);
            if (annotation == null) return;

            String operation = annotation.operation();

            // 5. 智能解析操作详情（根据参数类型自动生成）
            String detail = "";
            Object[] args = joinPoint.getArgs();

            if (args.length > 0) {
                Object firstArg = args[0];
                if (firstArg instanceof Student) {
                    Student s = (Student) firstArg;
                    detail = "姓名: " + s.getName() + ", 学号: " + s.getStudentId();
                } else if (firstArg instanceof Long) {
                    detail = "ID: " + firstArg;
                } else {
                    // 如果参数不是 Student 或 Long，则取注解中填写的 detail 字符串
                    detail = annotation.detail();
                }
            } else {
                detail = annotation.detail();
            }

            // 6. 构造并保存日志
            Log log = new Log();
            log.setUsername(username);
            log.setOperation(operation);
            log.setDetail(detail);
            log.setIp(ip);
            logService.save(log);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}