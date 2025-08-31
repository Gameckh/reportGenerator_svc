@echo off
chcp 65001 >nul
title Word报告生成器

echo ========================================
echo        Word报告生成器启动程序
echo ========================================
echo.

:: 检查Java环境
java -version >nul 2>&1
if errorlevel 1 (
    echo 错误：未检测到Java环境！
    echo 请确保已安装Java 8或更高版本。
    echo 下载地址：https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)

echo Java环境检查通过
echo.

:: 创建必要的目录
if not exist "data" mkdir data
if not exist "templates" mkdir templates
if not exist "reports" mkdir reports

echo 目录结构检查完成
echo.

:: 启动应用
echo 正在启动Word报告生成器...
echo 请稍候，启动完成后将自动打开浏览器...
echo.

java -jar WordReportGenerator.jar

:: 等待应用启动
timeout /t 5 /nobreak >nul

:: 自动打开浏览器
start http://localhost:8888

echo.
echo 应用已启动，浏览器将自动打开。
echo 如果没有自动打开，请手动访问：http://localhost:8888
echo.
echo 按任意键退出...
pause >nul
