package com.kevin.wordreportgenerator.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class ReportRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dataJsonPath; // 上传的data.json文件路径

    private String templateName;

    private String generatedReportPath;

    private LocalDateTime createdTime;
}
