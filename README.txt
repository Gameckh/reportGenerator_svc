Word报告生成器 - 使用说明
================================

【系统要求】
- Windows 7/8/10/11
- Java 8 或更高版本
- 至少 2GB 可用内存
- 至少 500MB 可用磁盘空间

【安装步骤】
1. 确保已安装Java环境
   - 下载地址：https://www.oracle.com/java/technologies/downloads/
   - 或使用OpenJDK：https://adoptium.net/

2. 解压程序包到任意目录
   - 建议解压到：C:\WordReportGenerator\

3. 双击运行 start.bat 启动程序
   - 程序会自动检查Java环境
   - 自动创建必要的目录
   - 启动后会自动打开浏览器

【使用说明】
1. 程序启动后，浏览器会自动打开 http://localhost:8888
2. 在Web界面中上传Word模板文件
3. 输入或上传数据
4. 点击生成报告
5. 下载生成的报告压缩包

【目录说明】
- data/          - 数据文件目录
- templates/     - 模板文件目录
- reports/       - 生成的报告目录（历史记录）
- config/        - 配置文件目录

【注意事项】
- 请勿删除或移动程序文件
- 程序运行时请保持网络连接
- 生成的报告会保留最近10次的历史记录
- 如需修改配置，请编辑 config/application.properties

【技术支持】
如有问题，请联系技术支持。

【版本信息】
版本：1.0.0
更新日期：2024年
