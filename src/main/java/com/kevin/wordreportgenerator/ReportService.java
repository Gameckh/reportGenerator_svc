package com.kevin.wordreportgenerator;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.PictureType;
import com.deepoove.poi.data.Pictures;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;

@Service
public class ReportService {
    @Value("${file.reports}")
    private String reportsDir;
    
    @Value("${file.reports.history-limit:10}")
    private int historyLimit;
    
    public String generateReports(ReportRequest request) throws Exception {
        // 创建以时间戳命名的子目录
        String timestamp = String.valueOf(System.currentTimeMillis());
        String outputDir = reportsDir + "/" + timestamp;
        String zipFilePath = outputDir + "/reports.zip";
        Files.createDirectories(Paths.get(outputDir));

        // 清理旧的生成目录（保留最多10次）
        cleanupOldDirectories();

        ObjectMapper mapper = new ObjectMapper();
        List<List<String>> data = mapper.readValue(request.getData(), List.class);

        List<Map<String, Object>> maps = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            List<String> item = data.get(i);
            Map<String, Object> map = new HashMap<>();
            for (int j = 0; j < item.size(); j++) {
                map.put(String.valueOf(j), item.get(j));
            }
//            map.put("QR", Pictures.ofBufferedImage(generateQRCode(item.get(1)), PictureType.PNG)
//                    .size(47, 47).create());
            maps.add(map);
        }

        for (int i = 0; i < maps.size(); i++) {
            String reportName = generateFileName(request, data.get(i), i);
            Map<String, Object> map = maps.get(i);
            XWPFTemplate.compile(request.getTemplatePath()).render(map).writeToFile(outputDir + "/" + reportName);
        }

        // 打包ZIP
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath));
        for (File file : Objects.requireNonNull(new File(outputDir).listFiles())) {
            // 跳过ZIP文件本身，避免嵌套压缩包
            if (file.getName().equals("reports.zip")) {
                continue;
            }
            FileInputStream fis = new FileInputStream(file);
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zipOut.putNextEntry(zipEntry);
            IOUtils.copy(fis, zipOut);
            fis.close();
        }
        zipOut.close();
        return zipFilePath;
    }

    /**
     * 生成文件名
     * @param request 请求参数
     * @param dataRow 数据行
     * @param index 数据行索引
     * @return 生成的文件名
     */
    private String generateFileName(ReportRequest request, List<String> dataRow, int index) {
        String fileName;
        
        // 如果指定了nameColumn，优先使用数据列的值作为文件名
        if (request.getNameColumn() != null && !request.getNameColumn().trim().isEmpty()) {
            try {
                int columnIndex = Integer.parseInt(request.getNameColumn());
                if (columnIndex >= 0 && columnIndex < dataRow.size()) {
                    String columnValue = dataRow.get(columnIndex);
                    // 清理文件名中的非法字符
                    fileName = sanitizeFileName(columnValue);
                } else {
                    // 列索引超出范围，使用默认命名
                    fileName = "report_" + (index + 1);
                }
            } catch (NumberFormatException e) {
                // 列索引格式错误，使用默认命名
                fileName = "report_" + (index + 1);
            }
        } else if (request.getBaseFileName() != null && !request.getBaseFileName().trim().isEmpty()) {
            // 使用基础文件名 + 序号的方式
            fileName = request.getBaseFileName() + "_" + (index + 1);
        } else {
            // 默认命名方式
            fileName = "report_" + (index + 1);
        }
        
        // 添加文件扩展名
        String extension = request.getFileExtension() != null ? request.getFileExtension() : ".docx";
        if (!extension.startsWith(".")) {
            extension = "." + extension;
        }
        
        return fileName + extension;
    }

    /**
     * 清理文件名中的非法字符
     * @param fileName 原始文件名
     * @return 清理后的文件名
     */
    private String sanitizeFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return "unnamed";
        }
        
        // 移除或替换Windows文件系统中的非法字符
        String sanitized = fileName.replaceAll("[<>:\"/\\\\|?*]", "_");
        
        // 移除前后空格
        sanitized = sanitized.trim();
        
        // 如果清理后为空，使用默认名称
        if (sanitized.isEmpty()) {
            return "unnamed";
        }
        
        // 限制文件名长度（Windows限制为255字符，这里设置为200以留有余量）
        if (sanitized.length() > 200) {
            sanitized = sanitized.substring(0, 200);
        }
        
        return sanitized;
    }

    /**
     * 清理旧的生成目录，保留最多指定次数的生成结果
     */
    private void cleanupOldDirectories() throws IOException {
        File reportsDirectory = new File(reportsDir);
        if (!reportsDirectory.exists()) {
            return;
        }

        // 获取所有子目录
        File[] subDirs = reportsDirectory.listFiles(File::isDirectory);
        if (subDirs == null || subDirs.length <= historyLimit) {
            return;
        }

        // 按修改时间排序，删除最早的目录
        Arrays.sort(subDirs, Comparator.comparingLong(File::lastModified));
        
        // 删除最早的目录，直到只剩下配置的数量
        int deleteCount = subDirs.length - historyLimit;
        for (int i = 0; i < deleteCount; i++) {
            deleteDirectoryRecursively(subDirs[i]);
        }
    }

    /**
     * 递归删除目录及其内容
     */
    private void deleteDirectoryRecursively(File directory) throws IOException {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectoryRecursively(file);
                    } else {
                        Files.delete(file.toPath());
                    }
                }
            }
            Files.delete(directory.toPath());
        }
    }

    public static BufferedImage generateQRCode(String text) throws IOException {
        QrConfig config = new QrConfig(300, 300);
        config.setMargin(0);
        BufferedImage bufferedImage = QrCodeUtil.generate(
                text,
                config
        );
        return bufferedImage;
    }
}
