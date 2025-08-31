@echo off
chcp 65001 >nul
title Vue前端集成脚本

echo ========================================
echo        Vue前端集成脚本
echo ========================================
echo.

:: 检查Vue项目目录
if not exist "vue-project" (
    echo 错误：未找到Vue项目目录！
    echo 请将Vue项目文件夹重命名为 "vue-project" 并放在当前目录下
    echo 或者修改此脚本中的路径
    pause
    exit /b 1
)

echo 找到Vue项目目录
echo.

:: 进入Vue项目目录
cd vue-project

:: 安装依赖
echo 正在安装Vue项目依赖...
call npm install
if errorlevel 1 (
    echo 错误：依赖安装失败！
    pause
    exit /b 1
)

:: 构建项目
echo 正在构建Vue项目...
call npm run build
if errorlevel 1 (
    echo 错误：项目构建失败！
    pause
    exit /b 1
)

:: 返回上级目录
cd ..

:: 复制构建结果到Spring Boot静态资源目录
echo 正在复制前端文件到Spring Boot项目...
if exist "vue-project\dist" (
    xcopy "vue-project\dist\*" "src\main\resources\static\" /E /Y /Q
    echo 前端文件复制完成！
) else (
    echo 错误：未找到构建输出目录 dist/
    pause
    exit /b 1
)

echo.
echo ========================================
echo 前端集成完成！
echo 现在可以构建Spring Boot项目了
echo ========================================
pause
