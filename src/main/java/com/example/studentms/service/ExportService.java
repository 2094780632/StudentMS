package com.example.studentms.service;

import java.io.IOException;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.example.studentms.entity.Student;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExportService {

    private final StudentService studentService;

    public void exportStudents(String keyword, HttpServletResponse response) throws IOException {
        List<Student> students = studentService.searchStudents(keyword, 0, Integer.MAX_VALUE).getContent();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("学生信息");

        // 表头
        XSSFRow headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("学号");
        headerRow.createCell(1).setCellValue("姓名");
        headerRow.createCell(2).setCellValue("性别");
        headerRow.createCell(3).setCellValue("年龄");
        headerRow.createCell(4).setCellValue("班级");
        headerRow.createCell(5).setCellValue("专业");

        // 数据行
        int rowIndex = 1;
        for (Student student : students) {
            XSSFRow row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(student.getStudentId());
            row.createCell(1).setCellValue(student.getName());
            row.createCell(2).setCellValue(student.getGender());
            row.createCell(3).setCellValue(student.getAge());
            row.createCell(4).setCellValue(student.getClassName());
            row.createCell(5).setCellValue(student.getMajor());
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=students.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
