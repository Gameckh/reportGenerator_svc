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
4. 配置报告文件命名方式（新增功能）
5. 点击生成报告
6. 下载生成的报告压缩包

【文件命名功能（新增）】
系统支持三种文件命名方式：

1. 使用数据列值命名
   - 当数据中包含唯一标识符（如员工编号、订单号）时
   - 在请求中指定 "nameColumn" 参数
   - 示例：使用员工编号命名，生成 "EMP001.docx", "EMP002.docx"

2. 使用基础文件名 + 序号
   - 指定 "baseFileName" 参数
   - 示例：生成 "员工报告_1.docx", "员工报告_2.docx"

3. 默认命名方式
   - 不指定命名参数时使用
   - 示例：生成 "report_1.docx", "report_2.docx"

详细使用说明请参考 USAGE_EXAMPLES.md 和 API_DESIGN.md

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
- 文件名会自动处理特殊字符，确保兼容性

【技术支持】
如有问题，请联系技术支持。

【版本信息】
版本：1.1.0
更新日期：2024年
新增功能：灵活的文件命名方式

【开发者说明】
如需重新构建前端（推荐方式）：
1. 在你的前端项目中运行：pnpm build
2. 将生成的dist文件夹拷贝到后端项目根目录
3. 运行 integrate-frontend-simple.bat 进行前端集成
4. 手动构建：mvn clean && mvn package -DskipTests
5. 手动创建发布包（参考INSTALL_GUIDE.md）

或者使用自动集成方式：
1. 确保已安装pnpm：npm install -g pnpm
2. 将前端项目文件夹重命名为 "reportGenerator_web" 并放在项目根目录
3. 运行 integrate-frontend.bat 进行前端集成
4. 手动构建：mvn clean && mvn package -DskipTests
5. 手动创建发布包（参考INSTALL_GUIDE.md）
