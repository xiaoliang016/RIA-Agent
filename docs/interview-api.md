# AI 模拟面试 API

面试业务入口位于 `ai-agent-station-study-trigger`，只负责会话参数、提示词和业务状态管理；真正的 Agent 路由、记忆、SSE、RunId 幂等和工具执行仍复用平台原有执行引擎。

## 创建会话

`POST /api/v1/interview/sessions`

请求示例：

```json
{
  "role": "Java 后端开发工程师",
  "experienceLevel": "1-3 年",
  "interviewType": "技术面试",
  "topics": ["java", "spring", "database"],
  "questionCount": 5,
  "userId": "10001"
}
```

`role` 必填，`questionCount` 范围为 1～20，未传时默认为 5。会话元数据默认在 Caffeine 中保存 12 小时，问答正文由底层会话记忆链路保存。

## 开始面试

`POST /api/v1/interview/sessions/{sessionId}/start`

返回 `text/event-stream`。AI 面试官提出第 1 道问题，前端可按 SSE 的 `data` 行读取 JSON，并将 `token` 字段拼接为完整文本。

## 提交答案

`POST /api/v1/interview/sessions/{sessionId}/answers`

请求示例：

```json
{
  "questionIndex": 1,
  "question": "请解释 Spring Bean 的生命周期。",
  "answer": "......",
  "expectedFocus": "生命周期、扩展点和项目实践",
  "userId": "10001"
}
```

答案最多 8000 字。返回流中包含四项评分（技术准确性、完整性、表达清晰度、实战深度）、改进建议以及下一道递进问题。

## 生成报告

`POST /api/v1/interview/sessions/{sessionId}/report`

返回 `text/event-stream`，根据当前会话中的全部问答生成综合得分、能力维度、技术优势、薄弱点和学习计划。

## 查询、结束和方向列表

```text
GET    /api/v1/interview/sessions/{sessionId}
DELETE /api/v1/interview/sessions/{sessionId}
GET    /api/v1/interview/topics
```

## 调用约定

- 可以在请求体或 `X-User-Id`、`X-Tenant-Id` 请求头中传入身份信息。
- 真实模型调用需要在 `.env` 配置有效的 `LLM_API_KEY`；占位 Key 只能验证服务启动和接口链路。
- 默认复用本地种子 Agent `8012`。如需为面试单独配置 Agent，可在 `InterviewController` 的 `dispatch` 方法中替换为专用 Agent ID。
