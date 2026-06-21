package com.example.studentms.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.studentms.annotation.LogAnnotation;
import com.example.studentms.service.ExportService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/export")
@RequiredArgsConstructor
public class ExportController {

    private final ExportService exportService;

    @LogAnnotation(operation = "导出学生数据")
    @GetMapping("/students")
    public void exportStudents(@RequestParam(required = false) String keyword,
                               HttpServletResponse response) throws IOException {
        exportService.exportStudents(keyword, response);
    }
}
