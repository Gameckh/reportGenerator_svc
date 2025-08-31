@echo off
chcp 65001 >nul
title 前端集成脚本（简化版）

echo ========================================
echo        前端集成脚本（简化版）
echo ========================================
echo.

:: 检查dist目录是否存在
if not exist "dist" (
    echo 错误：未找到dist目录！
    echo.
    echo 请按以下步骤操作：
    echo 1. 在你的前端项目中运行：pnpm build
    echo 2. 将生成的dist文件夹拷贝到当前目录（后端项目根目录）
    echo 3. 重新运行此脚本
    echo.
    pause
    exit /b 1
)

echo 找到dist目录
echo.

:: 检查Spring Boot静态资源目录
if not exist "src\main\resources\static" (
    echo 正在创建静态资源目录...
    mkdir "src\main\resources\static"
)

:: 复制dist内容到Spring Boot静态资源目录
echo 正在复制前端文件到Spring Boot项目...
xcopy "dist\*" "src\main\resources\static\" /E /Y /Q
if errorlevel 1 (
    echo 错误：文件复制失败！
    pause
    exit /b 1
)

echo 前端文件复制完成！
echo.

:: 清理临时dist目录（可选）
set /p choice="是否删除临时dist目录？(y/n): "
if /i "%choice%"=="y" (
    echo 正在删除临时dist目录...
    rmdir /s /q "dist"
    echo 临时目录已删除
)

echo.
echo ========================================
echo 前端集成完成！
echo 现在可以运行 build-release.bat 构建发布包了
echo ========================================
pause
