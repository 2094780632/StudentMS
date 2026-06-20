package com.example.studentms.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
    String operation() default "";  // 操作类型，如 "新增学生"
    String detail() default "";     // 操作详情，支持SpEL表达式
}