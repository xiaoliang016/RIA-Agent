# AI 模拟面试与求职辅导平台

基于 Spring Boot 3、Spring AI 和 DDD 分层架构打造的 AI 求职应用。平台把原有 Agent 编排引擎落地到模拟面试场景，支持按岗位生成问题、逐题评分、动态追问和面试复盘，也保留了 Fixed、Auto、Flow、多 Agent 路由、MCP、RAG、Redis 会话记忆和 SSE 流式输出等通用能力。

## 主要功能

- **岗位化模拟面试**：配置岗位、经验、面试类型和重点方向，自动生成贴合目标岗位的问题。
- **动态追问与评分**：候选人提交答案后，从技术准确性、完整性、表达清晰度、实战深度四个维度评分，并生成下一道递进问题。
- **面试报告**：根据整场问答输出综合得分、优势、薄弱点、改进建议和可执行学习计划。
- **Agent 编排底座**：统一路由 Fixed、Auto、Flow 三类执行策略，支持异步执行、RunId 幂等、取消、恢复和人工确认。
- **知识增强与记忆**：PostgreSQL + pgvector 提供向量检索，结合关键词检索和结果重排；Redis 保存会话上下文、运行快照和 SSE 流式状态。
- **工具与稳定性**：通过 MCP 接入外部工具，使用 Resilience4j 提供重试、熔断和限流，Actuator 暴露健康检查与指标。

## 模拟面试入口

启动后访问 [http://localhost:8099/interview.html](http://localhost:8099/interview.html)，或先打开首页再点击“AI 模拟面试”。页面支持岗位配置、重点方向选择、SSE 流式提问、答案提交和报告生成。

详细接口说明见 [docs/interview-api.md](docs/interview-api.md)。

## 技术栈

Java 17、Spring Boot 3.4、Spring AI、MyBatis、MySQL 8、PostgreSQL + pgvector、Redis、MCP SDK、Resilience4j、SSE、Docker Compose。

## 本地运行

项目自带 Docker Compose。需要 Java 17+、Maven 3.9+ 和 Docker Desktop：

```powershell
mvn '-Dmaven.test.skip=true' package
docker compose up -d
docker compose ps
```

访问地址：

- 模拟面试：[http://localhost:8099/interview.html](http://localhost:8099/interview.html)
- Agent 工作台：[http://localhost:8099/index.html](http://localhost:8099/index.html)
- Agent 配置：[http://localhost:8099/agent-config.html](http://localhost:8099/agent-config.html)
- 健康检查：[http://localhost:8099/actuator/health](http://localhost:8099/actuator/health)

默认网页登录账号：`admin` / `123456`。

如果默认端口被占用，可使用备用端口启动：

```powershell
$env:SMART_RD_APP_PORT=18099
$env:SMART_RD_MYSQL_PORT=23306
$env:SMART_RD_PG_PORT=25432
$env:SMART_RD_REDIS_PORT=26379
docker compose up -d
```

## DataGrip 连接

DataGrip 是数据库客户端，连接 Docker 暴露的端口：

| 数据库 | Host | Port | Database | User | Password |
| --- | --- | ---: | --- | --- | --- |
| MySQL 8 | `127.0.0.1` | `13306` | `ai-agent-station-study` | `root` | `123456` |
| PostgreSQL + pgvector | `127.0.0.1` | `15432` | `ai-rag-knowledge` | `postgres` | `123456` |
| Redis | `127.0.0.1` | `16379` | — | — | — |

## 模型配置

复制 `.env.example` 为 `.env`，填入有效的 `LLM_API_KEY`、`EMBEDDING_API_KEY`，并按服务商调整模型地址和模型名称。占位 Key 只能验证 Spring Boot 启动链路，不能生成真实面试内容。密钥通过环境变量注入，不要提交到 Git。

## 模块说明

| 模块 | 作用 |
| --- | --- |
| `ai-agent-station-study-api` | DTO、服务接口和统一响应对象 |
| `ai-agent-station-study-domain` | Agent 路由、执行策略、RAG、记忆和 MCP 治理 |
| `ai-agent-station-study-infrastructure` | MySQL、PostgreSQL、Redis 及外部适配器 |
| `ai-agent-station-study-trigger` | REST/SSE 接口、面试会话和业务提示词 |
| `ai-agent-station-study-app` | Spring Boot 启动模块和静态前端页面 |

## 简历项目描述（可直接使用）

> 基于 Spring Boot 3、Spring AI 和 DDD 多模块架构开发 AI 模拟面试与求职辅导平台，设计岗位化面试会话模型，支持动态提问、逐题评分、递进追问和自动生成面试报告；复用 Agent 路由与 Flow 编排引擎，结合 Redis 会话记忆、SSE 流式输出、PostgreSQL + pgvector 混合 RAG、MCP 工具调用及 Resilience4j 重试/熔断/限流，完成从面试配置到求职复盘的完整闭环。

本项目用于学习和展示 AI Agent 在 Java 后端业务中的工程化落地实践。
