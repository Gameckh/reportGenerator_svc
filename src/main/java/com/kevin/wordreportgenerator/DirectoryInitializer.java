package com.kevin.wordreportgenerator;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DirectoryInitializer {

    @Value("${file.template-dir}")
    private String templateDir;
    
    @Value("${file.data}")
    private String dataDir;
    
    @Value("${file.reports}")
    private String reportsDir;

    @PostConstruct
    public void init() {
        String[] dirs = {
                templateDir,
                dataDir,
                reportsDir
        };
        
        for (String dir : dirs) {
            File directory = new File(dir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
        }
    }
}
