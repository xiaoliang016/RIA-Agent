# 智能研发助手与 Agent 编排平台

基于 Spring Boot 3、Spring AI 和 DDD 分层架构的企业级 AI Agent 平台，面向研发团队提供知识库问答、GitHub Issue 分析和自动化工具调用能力。

## 项目定位

本项目在 TAgent Agent 执行引擎基础上进行了研发场景化改造：

- 用统一 Agent 路由处理 Fixed、Auto、Flow 三类任务。
- 通过 MCP 连接 GitHub 等外部工具，并支持按需动态补充工具。
- 使用 PostgreSQL + pgvector 构建研发知识库，输出可追溯的 RAG 证据。
- 使用 Redis 保存会话上下文和运行快照，支持 SSE 流式输出、任务恢复和步骤重做。
- 新增 GitHub Issue 分析业务入口，将仓库、Issue 和补充背景转换为结构化 Agent 任务。
- 使用 Resilience4j 实现模型调用重试、熔断和限流，并通过 Actuator 暴露健康检查和指标。

## GitHub Issue 分析接口

`POST /api/v1/developer-assistant/issues/analyze` 返回 `text/event-stream`，示例：

```json
{
  "repository": "owner/repository",
  "issueNumber": 42,
  "context": "接口在高并发下偶发 503，最近一次发布包含缓存改动",
  "sessionId": "demo-session"
}
```

接口会复用现有 Agent 执行链，依次完成 Issue 信息获取、问题分析、知识库检索和修复建议生成。`aiAgentId` 不填时使用本地种子数据中的多步骤 Flow Agent `8013`。

## 技术栈

Java 17、Spring Boot 3.4、Spring AI、MyBatis、MySQL 8、PostgreSQL + pgvector、Redis、MCP SDK、Resilience4j、SSE、Docker Compose。

## 本地运行

项目自带 `docker-compose.yml`，构建 JAR 后即可启动 MySQL、PostgreSQL + pgvector、Redis 和应用服务；首次初始化会自动执行研发助手所需的 MySQL 迁移。

```powershell
mvn '-Dmaven.test.skip=true' package
docker compose up -d
```

访问：

- 首页：<http://localhost:8099/index.html>
- Agent 配置：<http://localhost:8099/agent-config.html>
- 健康检查：<http://localhost:8099/actuator/health>

默认网页登录账号：`admin` / `123456`。数据库连接、初始化说明和模型配置请参考 [LOCAL-RUN.md](LOCAL-RUN.md)。

## 模型配置

启动时需要设置有效的 `LLM_API_KEY` 和 `EMBEDDING_API_KEY`。请复制 `.env.example` 为 `.env` 后填写；未填写时 Compose 使用占位值，仅用于验证 Spring Boot 启动链路，不能调用真实模型。密钥应通过环境变量注入，不要提交到 Git。

## 模块说明

| 模块 | 作用 |
| --- | --- |
| `ai-agent-station-study-api` | DTO、服务接口和统一响应对象 |
| `ai-agent-station-study-domain` | Agent 路由、执行策略、RAG、记忆和 MCP 治理 |
| `ai-agent-station-study-infrastructure` | MySQL、PostgreSQL、Redis 及外部适配器 |
| `ai-agent-station-study-trigger` | REST/SSE 接口、后台任务和研发助手业务入口 |
| `ai-agent-station-study-app` | Spring Boot 启动模块和运行配置 |
| `docs/dev-ops` | Docker、数据库迁移、监控和 MCP 配置 |

## 简历项目描述

> 基于 Spring Boot 3、Spring AI 和 DDD 架构开发智能研发助手与 Agent 编排平台，支持多 Agent 路由、GitHub MCP 工具调用、PostgreSQL + pgvector 知识库检索、Redis 运行快照和 SSE 流式交互；新增 GitHub Issue 分析业务入口，结合重试、熔断、限流和人工审批提升 Agent 任务执行的可靠性。

本项目用于学习和展示 AI Agent 的 Java 后端工程化落地实践。
