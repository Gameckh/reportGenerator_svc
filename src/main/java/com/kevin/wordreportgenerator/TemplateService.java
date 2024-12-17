package com.kevin.wordreportgenerator;

import com.kevin.wordreportgenerator.data.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TemplateService {

    @Autowired
    private TemplateRepository templateRepository;

    @Value("${file.template-dir}")
    private String templateDir;

    public Template uploadTemplate(String name, MultipartFile file) throws IOException {
        // 确保目录存在
        File dir = new File(templateDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 保存文件
        String filePath = templateDir + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File dest = new File(filePath);
        file.transferTo(dest);

        // 保存模板信息到数据库
        Template template = new Template();
        template.setName(name);
        template.setFilePath(filePath);
        template.setUploadTime(LocalDateTime.now().toString());

        return templateRepository.save(template);
    }

    public List<Template> getAllTemplates() {
        return templateRepository.findAll();
    }

    public Template getTemplateById(Long id) {
        return templateRepository.findById(id).orElse(null);
    }
}
