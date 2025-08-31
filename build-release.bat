@echo off
chcp 65001 >nul
title 构建发布包

echo ========================================
echo        Word报告生成器构建脚本
echo ========================================
echo.

:: 检查Maven环境
mvn -version >nul 2>&1
if errorlevel 1 (
    echo 错误：未检测到Maven环境！
    echo 请确保已安装Maven并配置环境变量
    pause
    exit /b 1
)

echo Maven环境检查通过
echo.

:: 清理之前的构建
echo 正在清理之前的构建...
call mvn clean
if errorlevel 1 (
    echo 错误：清理失败！
    pause
    exit /b 1
)

:: 构建项目
echo 正在构建Spring Boot项目...
call mvn package -DskipTests
if errorlevel 1 (
    echo 错误：构建失败！
    pause
    exit /b 1
)

:: 创建发布目录
echo 正在创建发布包...
if exist "release" rmdir /s /q "release"
mkdir release

:: 复制JAR文件
copy "target\WordReportGenerator-*.jar" "release\WordReportGenerator.jar"

:: 复制启动脚本
copy "start.bat" "release\"

:: 复制说明文档
copy "README.txt" "release\"

:: 创建配置目录
mkdir "release\config"
copy "src\main\resources\application.properties" "release\config\"

:: 创建数据目录
mkdir "release\data"
mkdir "release\templates"
mkdir "release\reports"

echo.
echo ========================================
echo 构建完成！
echo 发布包位置：release/
echo ========================================
echo.
echo 发布包包含以下文件：
echo - WordReportGenerator.jar    (主程序)
echo - start.bat                  (启动脚本)
echo - README.txt                 (使用说明)
echo - config/                    (配置目录)
echo - data/                      (数据目录)
echo - templates/                 (模板目录)
echo - reports/                   (报告目录)
echo.
echo 请将整个 release 目录打包给客户
pause
