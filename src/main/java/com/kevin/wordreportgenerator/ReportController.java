package com.kevin.wordreportgenerator;

import com.kevin.wordreportgenerator.data.ReportRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    // 提交生成报告任务
    @PostMapping("/generate")
    public ResponseEntity<ReportRecord> generateReport(@RequestParam("templateId") Long templateId,
                                                       @RequestParam("dataJson") MultipartFile dataJsonFile) {
        try {
            ReportRecord record = reportService.generateReport(templateId, dataJsonFile);
            return ResponseEntity.ok(record);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 获取所有报告记录
    @GetMapping
    public ResponseEntity<List<ReportRecord>> getAllReportRecords() {
        List<ReportRecord> records = reportService.getAllReportRecords();
        return ResponseEntity.ok(records);
    }

    // 下载生成的报告（压缩包）
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadReport(@PathVariable Long id) {
        ReportRecord record = reportService.getReportRecordById(id);
        if (record == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            File file = new File(record.getGeneratedReportPath());
            byte[] data = Files.readAllBytes(file.toPath());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + file.getName() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(data);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
