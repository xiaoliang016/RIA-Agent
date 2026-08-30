# 研发助手接口示例

## GitHub Issue 分析

接口：`POST /api/v1/developer-assistant/issues/analyze`

响应类型：`text/event-stream`

请求示例：

```json
{
  "repository": "spring-projects/spring-boot",
  "issueNumber": 12345,
  "context": "请重点关注最近一次发布后出现的连接池超时问题",
  "sessionId": "demo-session-001",
  "userId": "10001",
  "tenantId": "default"
}
```

服务会把请求转换为统一 `AutoAgentRequestDTO`，默认使用多步骤 Flow Agent `8013`，并复用 MCP、RAG、记忆、重试和 SSE 事件链。需要配置有效的 GitHub MCP 和模型密钥后，接口才会访问真实 GitHub 数据并生成模型分析结果。
