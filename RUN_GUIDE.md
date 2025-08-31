# Word报告生成器 - 运行指南

## 运行target目录中的JAR包

### 1. 直接运行JAR包

```bash
# 进入target目录
cd target

# 运行JAR包
java -jar WordReportGenerator-0.0.1-SNAPSHOT.jar
```

**注意**：应用启动时会自动创建以下目录：
- `./templates/` - 模板文件目录
- `./data/` - 数据文件目录  
- `./reports/` - 报告文件目录
- `./temp/` - 临时文件目录

启动日志会显示这些目录的绝对路径。

### 2. 指定配置文件运行

```bash
# 使用外部配置文件运行
java -jar WordReportGenerator-0.0.1-SNAPSHOT.jar --spring.config.location=file:./config/application.properties
```

### 3. 指定端口运行

```bash
# 指定端口运行
java -jar WordReportGenerator-0.0.1-SNAPSHOT.jar --server.port=9999
```

### 4. 后台运行（Windows）

```bash
# 后台运行
start /B java -jar WordReportGenerator-0.0.1-SNAPSHOT.jar
```

### 5. 使用启动脚本

如果你已经创建了发布包，可以直接使用启动脚本：

```bash
# 运行启动脚本
start.bat
```

## 运行参数说明

### 常用参数

- `--server.port=8888` - 指定服务器端口
- `--spring.config.location=file:./config/application.properties` - 指定配置文件位置
- `--file.template-dir=./templates/` - 指定模板目录
- `--file.data=./data/` - 指定数据目录
- `--file.reports=./reports/` - 指定报告目录

### 示例

```bash
# 完整参数示例
java -jar WordReportGenerator-0.0.1-SNAPSHOT.jar \
  --server.port=9999 \
  --spring.config.location=file:./config/application.properties \
  --file.template-dir=./templates/ \
  --file.data=./data/ \
  --file.reports=./reports/
```

## 访问应用

启动成功后，在浏览器中访问：

```
http://localhost:8888
```

## 停止应用

### 方法1：Ctrl+C
在运行JAR包的终端中按 `Ctrl+C`

### 方法2：查找进程并终止
```bash
# 查找Java进程
jps

# 终止进程（替换PID为实际进程ID）
taskkill /F /PID <PID>
```

## 日志查看

应用运行时的日志会显示在控制台中，包括：
- 启动信息
- 错误信息
- 访问日志

## 常见问题

### 1. 端口被占用
```
错误：Web server failed to start. Port 8888 was already in use.
解决：使用 --server.port=9999 指定其他端口
```

### 2. 内存不足
```
错误：java.lang.OutOfMemoryError
解决：增加JVM内存参数
java -Xmx2g -jar WordReportGenerator-0.0.1-SNAPSHOT.jar
```

### 3. 文件权限问题
```
错误：Permission denied
解决：确保有足够的文件读写权限
```

## 性能优化

### 增加JVM内存
```bash
java -Xms512m -Xmx2g -jar WordReportGenerator-0.0.1-SNAPSHOT.jar
```

### 启用GC日志
```bash
java -Xloggc:gc.log -jar WordReportGenerator-0.0.1-SNAPSHOT.jar
```

### 生产环境建议
```bash
java -server -Xms1g -Xmx2g -XX:+UseG1GC -jar WordReportGenerator-0.0.1-SNAPSHOT.jar
```
