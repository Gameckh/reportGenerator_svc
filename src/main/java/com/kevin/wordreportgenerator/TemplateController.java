package com.kevin.wordreportgenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.PostConstruct;

import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@RestController
@RequestMapping("/api/templates")
public class TemplateController {

    @Value("${file.template-dir}")
    private String templateDir;
    
    @PostConstruct
    public void init() {
        System.out.println("=== TemplateController 初始化 ===");
        System.out.println("模板目录配置: " + templateDir);
        System.out.println("模板目录绝对路径: " + Paths.get(templateDir).toAbsolutePath());
        System.out.println("================================");
    }
    
    @Autowired
    private TemplateService templateService;
    
    @Autowired
    private ReportService reportService;

    // 上传模板
    @PostMapping("/upload")
    public ResponseEntity<?> uploadTemplate(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file to upload");
        }
        
        try {
            // 验证文件类型
            String fileName = file.getOriginalFilename();
            if (fileName == null || !fileName.toLowerCase().endsWith(".docx")) {
                return ResponseEntity.badRequest().body("Only .docx files are allowed");
            }
            
            // 保存文件
            Path path = Paths.get(templateDir, fileName);
            Files.createDirectories(path.getParent());
            
            // 使用transferTo方法，更安全
            File destFile = path.toFile();
            file.transferTo(destFile);

            // 更新JSON记录
            templateService.addTemplateRecord(fileName, path.toString());
            return ResponseEntity.ok("Template uploaded successfully");
        } catch (IOException e) {
            e.printStackTrace(); // 添加控制台输出
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getMessage());
        }
    }

    // 获取所有模板
    @GetMapping("/list")
    public ResponseEntity<?> listTemplates() throws IOException {
        return ResponseEntity.ok(templateService.getAllTemplates());
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<?> deleteTemplate(@PathVariable String name) {
        try {
            if (templateService.deleteTemplate(name)) {
                return ResponseEntity.ok("Template deleted");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Template not found");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete failed");
        }
    }

    @GetMapping("/download/{name}")
    public ResponseEntity<?> downloadTemplate(@PathVariable String name) {
        try {
            Path filePath = Paths.get(templateDir, name);
            if (Files.exists(filePath)) {
                File file = filePath.toFile();

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                        .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                        .body(new InputStreamResource(new FileInputStream(file)));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Template not found");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to download template");
        }
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generateReports(@RequestBody ReportRequest request) {
        try {
            String zipPath = reportService.generateReports(request);
            File zipFile = new File(zipPath);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + zipFile.getName())
                    .body(new InputStreamResource(new FileInputStream(zipFile)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate reports");
        }
    }
}
