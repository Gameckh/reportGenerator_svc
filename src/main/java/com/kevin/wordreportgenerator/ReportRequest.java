package com.kevin.wordreportgenerator;

import java.util.List;

public class ReportRequest {
    private String templatePath; // 模板文件路径
    private List<List<String>> data; // 替换模板的动态数据
    private String nameColumn; // 用于命名的数据列索引（从0开始），如果指定则使用该列的值作为文件名
    private String baseFileName; // 基础文件名，如果指定则使用 baseFileName + 序号的方式命名
    private String fileExtension; // 文件扩展名，默认为 .docx
    private String qrCodeColumn; // 二维码列索引，如果指定则使用该列的值作为二维码

    // 默认构造函数
    public ReportRequest() {
        this.fileExtension = ".docx";
    }

    // Getters and Setters
    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public List<List<String>> getData() {
        return data;
    }

    public void setData(List<List<String>> data) {
        this.data = data;
    }

    public String getNameColumn() {
        return nameColumn;
    }

    public void setNameColumn(String nameColumn) {
        this.nameColumn = nameColumn;
    }

    public String getBaseFileName() {
        return baseFileName;
    }

    public void setBaseFileName(String baseFileName) {
        this.baseFileName = baseFileName;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getQrCodeColumn() {
        return qrCodeColumn;
    }

    public void setQrCodeColumn(String qrCodeColumn) {
        this.qrCodeColumn = qrCodeColumn;
    }
}

