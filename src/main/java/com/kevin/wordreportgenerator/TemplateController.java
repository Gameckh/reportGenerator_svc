package com.kevin.wordreportgenerator;

import com.kevin.wordreportgenerator.data.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/api/templates")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    // 上传模板
    @PostMapping("/upload")
    public ResponseEntity<Template> uploadTemplate(@RequestParam("name") String name,
                                                   @RequestParam("file") MultipartFile file) {
        // 验证文件类型
        if (!file.getOriginalFilename().endsWith(".docx")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // 验证文件大小（例如，不超过100MB）
        if (file.getSize() > 100 * 1024 * 1024) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        try {
            Template template = templateService.uploadTemplate(name, file);
            return ResponseEntity.ok(template);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 获取所有模板
    @GetMapping
    public ResponseEntity<List<Template>> getAllTemplates() {
        List<Template> templates = templateService.getAllTemplates();
        return ResponseEntity.ok(templates);
    }

    // 下载模板
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadTemplate(@PathVariable Long id) {
        Template template = templateService.getTemplateById(id);
        if (template == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            File file = new File(template.getFilePath());
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
