# Word报告生成器 - 安装和运行指南

## 系统要求

- **操作系统**: Windows 7/8/10/11
- **Java环境**: Java 8 或更高版本
- **内存**: 至少 2GB 可用内存
- **磁盘空间**: 至少 500MB 可用空间
- **网络**: 需要网络连接下载依赖

## 开发环境构建

### 1. 环境准备

确保已安装以下工具：
- **Java 8+**: [下载地址](https://www.oracle.com/java/technologies/downloads/)
- **Maven**: [下载地址](https://maven.apache.org/download.cgi)
- **pnpm**: `npm install -g pnpm`

### 2. 前端集成

#### 方式一：手动集成（推荐）

```bash
# 1. 在你的前端项目中构建
cd your-frontend-project
pnpm build

# 2. 将dist文件夹拷贝到后端项目根目录
# 例如：从 D:\projects\your-frontend\dist 拷贝到 D:\projects\geng_report\reportGenerator_svc\dist

# 3. 运行集成脚本
integrate-frontend-simple.bat
```

#### 方式二：自动集成

```bash
# 1. 将前端项目文件夹重命名为 "reportGenerator_web" 并放在后端项目根目录
# 2. 运行自动集成脚本
integrate-frontend.bat
```

### 3. 构建发布包

#### 方式一：手动构建（推荐）

```bash
# 1. 清理项目
mvn clean

# 2. 构建项目
mvn package -DskipTests

# 3. 手动创建发布包
mkdir release
copy target\WordReportGenerator-*.jar release\WordReportGenerator.jar
copy start.bat release\
copy README.txt release\
mkdir release\config
copy src\main\resources\application.properties release\config\
mkdir release\data
mkdir release\templates
mkdir release\reports
```

#### 方式二：使用IDE构建

在IDE中直接运行 `mvn package` 命令，然后在 `target` 目录中找到生成的JAR文件。

构建完成后，会在项目根目录下生成 `release` 文件夹。

## 客户安装和使用

### 1. 安装步骤

1. **安装Java环境**
   - 下载并安装Java 8或更高版本
   - 确保Java环境变量配置正确

2. **解压程序包**
   - 将release目录内容解压到任意目录
   - 建议解压到：`C:\WordReportGenerator\`

3. **启动程序**
   - 双击运行 `start.bat`
   - 程序会自动检查环境并启动

### 2. 使用说明

1. **启动程序**
   - 双击 `start.bat`
   - 等待程序启动（约30秒）
   - 浏览器会自动打开 `http://localhost:8888`

2. **使用流程**
   - 上传Word模板文件
   - 输入或上传数据
   - 点击生成报告
   - 下载生成的报告压缩包

### 3. 目录结构

```
WordReportGenerator/
├── WordReportGenerator.jar    # 主程序
├── start.bat                  # 启动脚本
├── README.txt                 # 使用说明
├── config/                    # 配置目录
│   └── application.properties # 配置文件
├── data/                      # 数据文件目录
├── templates/                 # 模板文件目录
└── reports/                   # 生成的报告目录（历史记录）
```

## 故障排除

### 常见问题

1. **Java环境问题**
   ```
   错误：未检测到Java环境！
   解决：安装Java 8+并配置环境变量
   ```

2. **端口占用问题**
   ```
   错误：端口8888被占用
   解决：修改config/application.properties中的server.port
   ```

3. **前端文件缺失**
   ```
   警告：未找到前端文件！
   解决：确保已正确集成前端文件
   ```

4. **构建失败**
   ```
   错误：构建失败！
   解决：检查Maven环境和网络连接
   ```

### 日志查看

程序运行日志会显示在控制台窗口中，如有问题请查看错误信息。

## 配置说明

### 修改配置

编辑 `config/application.properties` 文件：

```properties
# 服务器端口
server.port=8888

# 历史记录保留数量
file.reports.history-limit=10

# 文件上传大小限制
spring.servlet.multipart.max-file-size=10MB
```

### 自定义路径

可以修改文件存储路径：

```properties
# 模板目录
file.template-dir=./templates/

# 数据目录
file.data=./data/

# 报告目录
file.reports=./reports/
```

## 技术支持

如有问题，请联系技术支持团队。

---

**版本**: 1.0.0  
**更新日期**: 2024年
