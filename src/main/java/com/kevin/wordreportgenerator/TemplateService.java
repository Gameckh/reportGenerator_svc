package com.kevin.wordreportgenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TemplateService {
    @Value("${file.data}")
    private String dataDir;
    
    @Value("${file.template-dir}")
    private String templateDir;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void addTemplateRecord(String name, String path) throws IOException {
        List<Map<String, String>> templates = getAllTemplates();
        
        // 检查是否存在同名模板，如果存在则先删除旧记录
        templates = templates.stream()
                .filter(t -> !t.get("name").equals(name))
                .collect(Collectors.toList());
        
        Map<String, String> newTemplate = new HashMap<>();
        newTemplate.put("name", name);
        newTemplate.put("path", path);
        templates.add(newTemplate);

        Files.write(Paths.get(dataDir + "/templates.json"), objectMapper.writeValueAsBytes(templates));
    }

    public List<Map<String, String>> getAllTemplates() throws IOException {
        Path filePath = Paths.get(dataDir + "/templates.json");
        if (!Files.exists(filePath)) {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, "[]".getBytes());
        }
        return objectMapper.readValue(Files.readAllBytes(filePath), List.class);
    }

    // 删除模板文件和记录
    public boolean deleteTemplate(String name) throws IOException {
        List<Map<String, String>> templates = getAllTemplates();

        // 查找并删除记录
        Optional<Map<String, String>> templateToDelete = templates.stream()
                .filter(t -> t.get("name").equals(name))
                .findFirst();

        if (templateToDelete.isPresent()) {
            // 删除文件
            Path templatePath = Paths.get(templateDir + name);
            Files.deleteIfExists(templatePath);

            // 更新JSON记录
            List<Map<String, String>> updatedTemplates = templates.stream()
                    .filter(t -> !t.get("name").equals(name))
                    .collect(Collectors.toList());

            Files.write(Paths.get(dataDir + "/templates.json"), objectMapper.writeValueAsBytes(updatedTemplates));
            return true;
        }

        return false;
    }

}