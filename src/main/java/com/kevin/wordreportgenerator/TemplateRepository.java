package com.kevin.wordreportgenerator;

import com.kevin.wordreportgenerator.data.Template;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateRepository extends JpaRepository<Template, Long> {
    // 根据名称查找模板
    Template findByName(String name);
}
