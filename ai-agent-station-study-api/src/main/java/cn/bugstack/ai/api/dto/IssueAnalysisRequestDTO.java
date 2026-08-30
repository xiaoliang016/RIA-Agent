package cn.bugstack.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * GitHub Issue 分析请求。
 *
 * <p>这是智能研发助手面向研发场景的业务入口，底层仍复用统一 Agent
 * 路由、MCP 工具调用和 SSE 流式执行链。</p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssueAnalysisRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** GitHub 仓库，格式为 owner/name。 */
    private String repository;

    /** Issue 编号。 */
    private Long issueNumber;

    /** 可选的 Issue URL；填写后 Agent 可优先使用 URL 定位问题。 */
    private String issueUrl;

    /** 可选的补充背景，例如运行日志、复现步骤或业务约束。 */
    private String context;

    /** 可选 Agent，不填时使用研发分析 Flow Agent。 */
    private String aiAgentId;

    /** 会话 ID，用于保留多轮研发上下文。 */
    private String sessionId;

    private String userId;

    private String tenantId;
}
