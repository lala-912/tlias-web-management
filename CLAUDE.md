# CLAUDE.md

## 技术栈
- **后端**：Spring Boot + MyBatis + MySQL
- **鉴权**：JWT（令牌验证）
- **文件存储**：阿里云 OSS
- **部署**：Docker（MySQL/Nginx）
- **构建工具**：Maven
- **包名**：`com.chy`

## 项目结构
```
tlias-web-management/
├── aliyun-oss-spring-boot-autoconfigure/   # OSS 自动配置
├── aliyun-oss-springboot-starter/          # OSS starter
└── tlias-web-management/                   # 主模块
    └── src/main/java/com/chy/
        ├── anno/          # 自定义注解
        ├── aop/           # AOP 切面（日志等）
        ├── config/        # 配置类
        ├── controller/    # 控制器
        ├── exception/     # 异常处理
        ├── filter/        # 过滤器（JWT 鉴权）
        ├── interceptor/   # 拦截器
        ├── mapper/        # MyBatis Mapper
        ├── pojo/          # 实体/VO/DTO
        ├── service/       # 业务接口
        │   └── impl/      # 业务实现
        └── utils/         # 工具类
```

## Bash 命令规范

- **优先使用专用工具**：读文件用 `Read`，编辑用 `Edit`，新建用 `Write`，搜索用 `grep`/`find`。Bash 仅用于无法通过专用工具完成的操作（如 git 操作、Maven 构建、Docker 命令等）。
- **并行执行**：多个独立命令同时发送，不要串行等待。有依赖关系的命令用 `&&` 链式执行。
- **安全第一**：绝不使用 `--no-verify` 跳过钩子，绝不 force push 到 main/master。执行破坏性操作前必须确认。
- **git 操作**：创建新 commit 而非 amend。commit message 用中文写清楚"为什么"而非"改了什么"。git add 时指定具体文件而非 `git add -A`。

## 代码风格

- **命名**：
  - 类名：`PascalCase`（如 `EmpController`、`ClazzMapper`）
  - 方法/变量：`camelCase`（如 `getEmpList`、`pageSize`）
  - 常量：`UPPER_SNAKE_CASE`
  - 文件/目录：`kebab-case`
- **注释**：默认不写注释。仅当 WHY 不显而易见的场景添加。不要写多段 docstring — 最多一行。
- **代码简洁**：不提前抽象。三个相似行优于一个过早封装。不添加不可能发生的错误处理。
- **安全**：始终防范 SQL 注入（MyBatis `#{}` 而非 `${}`）、XSS 等漏洞。JWT 密钥不硬编码。
- **分层规范**：
  - Controller：接收请求、调用 service、返回结果
  - Service：业务逻辑
  - Mapper：数据库操作（SQL 写在 XML 或注解中）
  - POJO：数据载体，不含业务逻辑

## 工作流程

- **复杂任务前先规划**：涉及 3+ 文件或多种实现方案的变更，先调用 `EnterPlanMode` 制定计划。
- **逐步确认**：大任务拆分为可验证的小步骤，每步完成后更新任务状态。
- **测试优先**：修改后运行 `mvn test` 验证。涉及 API 变更时检查相关 controller 调用链。
- **中文注释**：关键逻辑、复杂算法处用中文注释说明意图。
- **沟通简洁**：一条消息只聚焦一个目标。不要输出思考过程。

## 常用命令

```bash
# 构建
mvn clean package -DskipTests

# 运行测试
mvn test

# 本地运行
mvn spring-boot:run

# Docker 部署
docker-compose up -d
```
