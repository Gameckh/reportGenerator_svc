package com.kevin.wordreportgenerator;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.PictureType;
import com.deepoove.poi.data.Pictures;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevin.wordreportgenerator.data.ReportRecord;
import com.kevin.wordreportgenerator.data.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReportService {

    @Autowired
    private ReportRecordRepository reportRecordRepository;

    @Autowired
    private TemplateRepository templateRepository;

    private final String reportDir = "generated_reports/";
    private final String dataDir = "data_json/";

    public ReportRecord generateReport(Long templateId, MultipartFile dataJsonFile) throws IOException {
        // 获取模板
        Template template = templateRepository.findById(templateId).orElseThrow(() -> new RuntimeException("模板不存在"));

        // 保存data.json
        File dataDirFile = new File(dataDir);
        if (!dataDirFile.exists()) {
            dataDirFile.mkdirs();
        }
        String dataJsonPath = dataDir + System.currentTimeMillis() + "_" + dataJsonFile.getOriginalFilename();
        File destDataJson = new File(dataJsonPath);
        dataJsonFile.transferTo(destDataJson);

        // 读取data.json
        ObjectMapper mapper = new ObjectMapper();
        List<List<String>> data = mapper.readValue(destDataJson, List.class);
        List<Map<String, Object>> maps = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            List<String> item = data.get(i);
            Map<String, Object> map = new HashMap<>();
            for (int j = 0; j < item.size(); j++) {
                map.put(String.valueOf(j), item.get(j));
            }
            // 生成二维码
            BufferedImage qrImage = generateQRCode(item.get(1));
            map.put("QR", Pictures.ofBufferedImage(qrImage, PictureType.PNG).size(47, 47).create());
            maps.add(map);
        }

        // 生成报告
        File reportDirFile = new File(reportDir);
        if (!reportDirFile.exists()) {
            reportDirFile.mkdirs();
        }

        List<String> generatedReportPaths = new ArrayList<>();
        for (int i = 0; i < maps.size(); i++) {
            Map<String, Object> map = maps.get(i);
            XWPFTemplate.compile(template.getFilePath()).render(map);
            String reportPath = reportDir + "报告_" + (i + 1) + "_" + System.currentTimeMillis() + ".docx";
            XWPFTemplate.compile(template.getFilePath()).render(map).writeToFile(reportPath);
            generatedReportPaths.add(reportPath);
        }

        // 压缩报告
        String zipPath = reportDir + "reports_" + System.currentTimeMillis() + ".zip";
        zipFiles(generatedReportPaths, zipPath);

        // 保存报告记录
        ReportRecord record = new ReportRecord();
        record.setDataJsonPath(dataJsonPath);
        record.setTemplateName(template.getName());
        record.setGeneratedReportPath(zipPath);
        record.setCreatedTime(LocalDateTime.now());
        return reportRecordRepository.save(record);
    }

    private BufferedImage generateQRCode(String text) throws IOException {
        // 使用Hutool生成二维码
        QrConfig config = new QrConfig(300, 300);
        config.setMargin(0);
        BufferedImage bufferedImage = QrCodeUtil.generate(text, config);
        return bufferedImage;
    }

    private void zipFiles(List<String> files, String zipPath) throws IOException {
        try (
                FileOutputStream fos = new FileOutputStream(zipPath);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream(bos)
        ) {
            for (String filePath : files) {
                File file = new File(filePath);
                if (!file.exists()) continue;
                try (FileInputStream fis = new FileInputStream(file)) {
                    java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(file.getName());
                    zos.putNextEntry(zipEntry);
                    byte[] bytes = new byte[1024];
                    int length;
                    while((length = fis.read(bytes)) >= 0) {
                        zos.write(bytes, 0, length);
                    }
                }
            }
        }
    }

    public List<ReportRecord> getAllReportRecords() {
        return reportRecordRepository.findAll();
    }

    public ReportRecord getReportRecordById(Long id) {
        return reportRecordRepository.findById(id).orElse(null);
    }
}
