# 本地运行说明

## 构建

项目需要 Java 17 或更高版本、Maven 3.9+ 和 Docker Desktop。

```powershell
mvn '-Dmaven.test.skip=true' package
```

## Docker Compose

在仓库根目录执行：

```powershell
docker compose up -d
docker compose ps
```

如果工作区里原来的 TAgent 编排仍占用 `8099/13306/15432/16379`，可以先停止原编排，也可以为本项目指定备用宿主机端口：

```powershell
docker compose -f ..\docker-compose.tagent.yml stop
```

并行启动示例：

```powershell
$env:SMART_RD_APP_PORT=18099
$env:SMART_RD_MYSQL_PORT=23306
$env:SMART_RD_PG_PORT=25432
$env:SMART_RD_REDIS_PORT=26379
docker compose up -d
```

Compose 会启动 MySQL、PostgreSQL + pgvector、Redis 和智能研发助手应用，并在首次创建数据库卷时自动加载基础表、模型档位、记忆、评测和后台任务表。数据库数据保存在 Docker named volumes 中；日常停止请使用 `docker compose stop`，不要随意执行 `down -v`。

## 访问地址

- 首页：<http://localhost:8099/index.html>
- Agent 配置：<http://localhost:8099/agent-config.html>
- 健康检查：<http://localhost:8099/actuator/health>
- GitHub Issue 分析：`POST http://localhost:8099/api/v1/developer-assistant/issues/analyze`

网页登录账号：`admin` / `123456`。

## DataGrip 连接

DataGrip 是数据库客户端，连接 Docker 暴露的端口：

| 数据库 | Host | Port | Database | User | Password |
| --- | --- | ---: | --- | --- | --- |
| MySQL 8 | `127.0.0.1` | `13306` | `ai-agent-station-study` | `root` | `123456` |
| PostgreSQL + pgvector | `127.0.0.1` | `15432` | `ai-rag-knowledge` | `postgres` | `123456` |
| Redis | `127.0.0.1` | `16379` | — | — | — |

## 模型配置

复制 `.env.example` 为 `.env`，配置有效的 `LLM_API_KEY`、`EMBEDDING_API_KEY`，并按服务商调整 `LLM_BASE_URL`、`LLM_MODEL`、`EMBEDDING_BASE_URL` 和 `EMBEDDING_MODEL`。密钥只通过环境变量注入，不要提交到 Git。
