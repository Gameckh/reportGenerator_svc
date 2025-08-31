# 报告生成API设计文档

## 接口概述

报告生成API支持灵活的文档命名方式，满足客户的不同需求场景。

## 请求参数

### ReportRequest 类

```json
{
  "templatePath": "模板文件路径",
  "data": "JSON格式的数据数组",
  "nameColumn": "用于命名的数据列索引（可选）",
  "baseFileName": "基础文件名（可选）",
  "fileExtension": "文件扩展名（可选，默认为.docx）"
}
```

### 参数说明

- **templatePath** (必需): 模板文件的路径
- **data** (必需): JSON格式的数据数组，用于替换模板中的变量
- **nameColumn** (可选): 用于命名的数据列索引，从0开始计数。如果指定，将使用该列的值作为文件名
- **baseFileName** (可选): 基础文件名。如果指定且未指定nameColumn，将使用 baseFileName + 序号的方式命名
- **fileExtension** (可选): 文件扩展名，默认为 ".docx"

## 命名规则

### 场景1: 使用数据列值命名
当指定 `nameColumn` 参数时，系统将使用该列的值作为文件名。

**示例请求:**
```json
{
  "templatePath": "templates/report_template.docx",
  "data": "[{\"编号\": \"001\", \"姓名\": \"张三\", \"部门\": \"技术部\"}, {\"编号\": \"002\", \"姓名\": \"李四\", \"部门\": \"销售部\"}]",
  "nameColumn": "0"
}
```

**生成的文件名:**
- `001.docx`
- `002.docx`

### 场景2: 使用基础文件名 + 序号
当指定 `baseFileName` 参数且未指定 `nameColumn` 时，系统将使用基础文件名 + 序号的方式命名。

**示例请求:**
```json
{
  "templatePath": "templates/report_template.docx",
  "data": "[{\"编号\": \"001\", \"姓名\": \"张三\"}, {\"编号\": \"002\", \"姓名\": \"李四\"}]",
  "baseFileName": "员工报告"
}
```

**生成的文件名:**
- `员工报告_1.docx`
- `员工报告_2.docx`

### 场景3: 默认命名
当未指定 `nameColumn` 和 `baseFileName` 时，系统使用默认的命名方式。

**示例请求:**
```json
{
  "templatePath": "templates/report_template.docx",
  "data": "[{\"编号\": \"001\", \"姓名\": \"张三\"}, {\"编号\": \"002\", \"姓名\": \"李四\"}]"
}
```

**生成的文件名:**
- `report_1.docx`
- `report_2.docx`

## 文件名安全处理

系统会自动处理文件名中的非法字符：

1. **非法字符替换**: 将 `<>:"/\|?*` 等Windows文件系统非法字符替换为下划线 `_`
2. **长度限制**: 文件名长度限制为200字符
3. **空值处理**: 如果列值为空或清理后为空，使用 "unnamed" 作为文件名
4. **扩展名处理**: 自动添加文件扩展名，如果未指定则默认为 ".docx"

## 优先级规则

1. 如果指定了 `nameColumn`，优先使用数据列值命名
2. 如果未指定 `nameColumn` 但指定了 `baseFileName`，使用基础文件名 + 序号命名
3. 如果都未指定，使用默认的 "report_序号" 命名方式

## 错误处理

- **列索引超出范围**: 如果指定的列索引超出数据范围，使用默认命名
- **列索引格式错误**: 如果列索引不是有效数字，使用默认命名
- **空值或无效值**: 如果列值为空或包含无效字符，使用 "unnamed" 作为文件名

## 使用示例

### 使用员工编号命名
```bash
curl -X POST http://localhost:8080/api/reports/generate \
  -H "Content-Type: application/json" \
  -d '{
    "templatePath": "templates/employee_report.docx",
    "data": "[{\"编号\": \"EMP001\", \"姓名\": \"张三\"}, {\"编号\": \"EMP002\", \"姓名\": \"李四\"}]",
    "nameColumn": "0"
  }'
```

### 使用自定义基础文件名
```bash
curl -X POST http://localhost:8080/api/reports/generate \
  -H "Content-Type: application/json" \
  -d '{
    "templatePath": "templates/employee_report.docx",
    "data": "[{\"编号\": \"EMP001\", \"姓名\": \"张三\"}, {\"编号\": \"EMP002\", \"姓名\": \"李四\"}]",
    "baseFileName": "员工月度报告"
  }'
```

### 指定自定义文件扩展名
```bash
curl -X POST http://localhost:8080/api/reports/generate \
  -H "Content-Type: application/json" \
  -d '{
    "templatePath": "templates/employee_report.docx",
    "data": "[{\"编号\": \"EMP001\", \"姓名\": \"张三\"}]",
    "baseFileName": "员工报告",
    "fileExtension": ".pdf"
  }'
```
