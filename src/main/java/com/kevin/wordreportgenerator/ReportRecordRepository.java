package com.kevin.wordreportgenerator;

import com.kevin.wordreportgenerator.data.ReportRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRecordRepository extends JpaRepository<ReportRecord, Long> {
    // 可根据需求添加自定义查询方法
}
