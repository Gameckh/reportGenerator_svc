# 报告生成接口使用示例

## 概述

本文档提供了报告生成接口的详细使用示例，展示如何根据不同场景配置文档命名方式。

## 基础配置

### 1. 使用数据列值命名（推荐场景）

当您的数据中包含唯一标识符（如员工编号、订单号等）时，可以使用该列的值作为文件名。

**请求示例：**
```json
{
  "templatePath": "templates/employee_report.docx",
  "data": [
    ["EMP001", "张三", "技术部", "高级工程师"],
    ["EMP002", "李四", "销售部", "销售经理"],
    ["EMP003", "王五", "人事部", "人事专员"]
  ],
  "nameColumn": "0"
}
```

**生成的文件：**
- `EMP001.docx`
- `EMP002.docx`
- `EMP003.docx`

### 2. 使用基础文件名 + 序号

当您希望使用统一的命名格式时，可以指定基础文件名。

**请求示例：**
```json
{
  "templatePath": "templates/employee_report.docx",
  "data": [
    ["EMP001", "张三", "技术部", "高级工程师"],
    ["EMP002", "李四", "销售部", "销售经理"],
    ["EMP003", "王五", "人事部", "人事专员"]
  ],
  "baseFileName": "员工月度报告"
}
```

**生成的文件：**
- `员工月度报告_1.docx`
- `员工月度报告_2.docx`
- `员工月度报告_3.docx`

### 3. 默认命名方式

如果不指定任何命名参数，系统将使用默认的命名方式。

**请求示例：**
```json
{
  "templatePath": "templates/employee_report.docx",
  "data": [
    ["EMP001", "张三", "技术部", "高级工程师"],
    ["EMP002", "李四", "销售部", "销售经理"],
    ["EMP003", "王五", "人事部", "人事专员"]
  ]
}
```

**生成的文件：**
- `report_1.docx`
- `report_2.docx`
- `report_3.docx`

## 高级配置

### 自定义文件扩展名

您可以指定不同的文件扩展名。

**请求示例：**
```json
{
  "templatePath": "templates/employee_report.docx",
  "data": [
    ["EMP001", "张三", "技术部", "高级工程师"]
  ],
  "baseFileName": "员工报告",
  "fileExtension": ".pdf"
}
```

**生成的文件：**
- `员工报告_1.pdf`

### 处理特殊字符

系统会自动处理文件名中的特殊字符。

**请求示例：**
```json
{
  "templatePath": "templates/employee_report.docx",
  "data": [
    ["EMP001<test>", "张三", "技术部", "高级工程师"],
    ["EMP002:test", "李四", "销售部", "销售经理"]
  ],
  "nameColumn": "0"
}
```

**生成的文件：**
- `EMP001_test_.docx`
- `EMP002_test.docx`

## 实际应用场景

### 场景1：员工档案报告

**需求：** 为每个员工生成个人档案报告，使用员工编号作为文件名。

**请求：**
```json
{
  "templatePath": "templates/employee_profile.docx",
  "data": [
    ["EMP2024001", "张三", "技术部", "高级工程师", "2024-01-15"],
    ["EMP2024002", "李四", "销售部", "销售经理", "2024-01-20"],
    ["EMP2024003", "王五", "人事部", "人事专员", "2024-01-25"]
  ],
  "nameColumn": "0"
}
```

**结果：**
- `EMP2024001.docx`
- `EMP2024002.docx`
- `EMP2024003.docx`

### 场景2：订单报告

**需求：** 为每个订单生成报告，使用订单号作为文件名。

**请求：**
```json
{
  "templatePath": "templates/order_report.docx",
  "data": [
    ["ORD202401001", "张三", "笔记本电脑", "8999.00", "2024-01-15"],
    ["ORD202401002", "李四", "办公椅", "599.00", "2024-01-16"],
    ["ORD202401003", "王五", "打印机", "1299.00", "2024-01-17"]
  ],
  "nameColumn": "0"
}
```

**结果：**
- `ORD202401001.docx`
- `ORD202401002.docx`
- `ORD202401003.docx`

### 场景3：月度总结报告

**需求：** 为每个部门生成月度总结报告，使用统一的命名格式。

**请求：**
```json
{
  "templatePath": "templates/monthly_summary.docx",
  "data": [
    ["技术部", "15", "120000", "95%"],
    ["销售部", "8", "80000", "88%"],
    ["人事部", "5", "40000", "92%"]
  ],
  "baseFileName": "2024年1月部门总结"
}
```

**结果：**
- `2024年1月部门总结_1.docx`
- `2024年1月部门总结_2.docx`
- `2024年1月部门总结_3.docx`

## 错误处理示例

### 列索引超出范围

**请求：**
```json
{
  "templatePath": "templates/employee_report.docx",
  "data": [
    ["EMP001", "张三", "技术部"]
  ],
  "nameColumn": "10"
}
```

**结果：** 使用默认命名 `report_1.docx`

### 空值处理

**请求：**
```json
{
  "templatePath": "templates/employee_report.docx",
  "data": [
    ["", "张三", "技术部"],
    ["EMP002", "李四", "销售部"]
  ],
  "nameColumn": "0"
}
```

**结果：**
- `unnamed.docx`
- `EMP002.docx`

## 最佳实践

1. **优先使用数据列命名**：当数据中包含唯一标识符时，建议使用 `nameColumn` 参数
2. **合理选择列索引**：确保选择的列包含有意义且唯一的标识符
3. **处理特殊字符**：系统会自动处理特殊字符，但建议在数据源中避免使用非法字符
4. **文件扩展名**：如果不指定，默认为 `.docx`，可以根据需要自定义
5. **错误处理**：系统具有完善的错误处理机制，会自动降级到默认命名方式

## API调用示例

### 使用curl命令

```bash
# 使用员工编号命名
curl -X POST http://localhost:8080/api/reports/generate \
  -H "Content-Type: application/json" \
  -d '{
    "templatePath": "templates/employee_report.docx",
    "data": [["EMP001", "张三", "技术部"], ["EMP002", "李四", "销售部"]],
    "nameColumn": "0"
  }'

# 使用基础文件名命名
curl -X POST http://localhost:8080/api/reports/generate \
  -H "Content-Type: application/json" \
  -d '{
    "templatePath": "templates/employee_report.docx",
    "data": [["EMP001", "张三", "技术部"], ["EMP002", "李四", "销售部"]],
    "baseFileName": "员工报告"
  }'
```

### 使用JavaScript

```javascript
// 使用员工编号命名
const response = await fetch('/api/reports/generate', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    templatePath: 'templates/employee_report.docx',
    data: [
      ['EMP001', '张三', '技术部'],
      ['EMP002', '李四', '销售部']
    ],
    nameColumn: '0'
  })
});

// 使用基础文件名命名
const response2 = await fetch('/api/reports/generate', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    templatePath: 'templates/employee_report.docx',
    data: [
      ['EMP001', '张三', '技术部'],
      ['EMP002', '李四', '销售部']
    ],
    baseFileName: '员工报告'
  })
});
```
