package com.kevin.wordreportgenerator;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.servlet.MultipartConfigElement;
import java.io.File;

@Configuration
public class FileUploadConfig {

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        
        // 设置文件大小限制
        factory.setMaxFileSize(DataSize.ofMegabytes(10));
        factory.setMaxRequestSize(DataSize.ofMegabytes(10));
        
        // 设置文件大小阈值，超过此值将写入磁盘
        factory.setFileSizeThreshold(DataSize.ofKilobytes(2));
        
        // 设置临时文件目录
        String tempDir = System.getProperty("java.io.tmpdir") + "/wordreportgenerator";
        File tempFile = new File(tempDir);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        factory.setLocation(tempDir);
        
        return factory.createMultipartConfig();
    }
}
