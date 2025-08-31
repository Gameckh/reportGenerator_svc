package com.kevin.wordreportgenerator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class DirectoryInitializer implements CommandLineRunner {

    @Value("${file.template-dir}")
    private String templateDir;

    @Value("${file.data}")
    private String dataDir;

    @Value("${file.reports}")
    private String reportsDir;

    @Override
    public void run(String... args) throws Exception {
        // 创建必要的目录
        createDirectoryIfNotExists(templateDir);
        createDirectoryIfNotExists(dataDir);
        createDirectoryIfNotExists(reportsDir);
        
        System.out.println("=== 目录初始化完成 ===");
        System.out.println("模板目录: " + getAbsolutePath(templateDir));
        System.out.println("数据目录: " + getAbsolutePath(dataDir));
        System.out.println("报告目录: " + getAbsolutePath(reportsDir));
        System.out.println("=====================");
    }

    private void createDirectoryIfNotExists(String dirPath) throws Exception {
        Path path = Paths.get(dirPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
            System.out.println("创建目录: " + getAbsolutePath(dirPath));
        }
    }

    private String getAbsolutePath(String relativePath) {
        try {
            return Paths.get(relativePath).toAbsolutePath().toString();
        } catch (Exception e) {
            return relativePath;
        }
    }
}
