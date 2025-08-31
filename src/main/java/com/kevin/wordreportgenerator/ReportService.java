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
            String reportName = "report_" + (i+1) + ".docx";
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
