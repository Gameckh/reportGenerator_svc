package com.kevin.wordreportgenerator.data;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String filePath; // 存储模板文件的路径

    private String uploadTime;
}
