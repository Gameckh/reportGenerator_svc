package com.kevin.wordreportgenerator;

import com.kevin.wordreportgenerator.ReportService;
import com.kevin.wordreportgenerator.ReportRequest;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateReports(@RequestBody ReportRequest request) {
        File zipFile = null;
        try {
            // 调用 ReportService 生成 ZIP 文件
            String zipFilePath = reportService.generateReports(request);
            zipFile = new File(zipFilePath);

            // 读取生成的 ZIP 文件并返回
            FileInputStream fileInputStream = new FileInputStream(zipFile);
            byte[] zipContent = IOUtils.toByteArray(fileInputStream);
            fileInputStream.close();

            // 删除ZIP文件以节省空间（保留报告文件用于历史记录）
            if (zipFile.exists()) {
                zipFile.delete();
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + zipFile.getName())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(zipContent);

        } catch (FileNotFoundException e) {
            return ResponseEntity.status(404).body("ZIP file not found");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error generating reports");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error");
        } finally {
            // 确保在异常情况下也删除ZIP文件
            if (zipFile != null && zipFile.exists()) {
                zipFile.delete();
            }
        }
    }
}
