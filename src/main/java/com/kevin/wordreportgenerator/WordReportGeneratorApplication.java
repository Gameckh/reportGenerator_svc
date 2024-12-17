package com.kevin.wordreportgenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class WordReportGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(WordReportGeneratorApplication.class, args);
    }

}