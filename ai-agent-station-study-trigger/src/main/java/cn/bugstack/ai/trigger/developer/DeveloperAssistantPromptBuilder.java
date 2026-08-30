package cn.bugstack.ai.trigger.developer;

import cn.bugstack.ai.api.dto.IssueAnalysisRequestDTO;
import org.springframework.util.StringUtils;

/**
 * 将研发场景请求转换为结构化 Agent 指令，避免 Controller 中散落提示词拼接逻辑。
 */
public final class DeveloperAssistantPromptBuilder {

    private static final int MAX_CONTEXT_CHARS = 4_000;

    private DeveloperAssistantPromptBuilder() {
    }

    public static String buildIssueAnalysisPrompt(IssueAnalysisRequestDTO request) {
        String repository = required(request.getRepository(), "repository");
        if (request.getIssueNumber() == null && !StringUtils.hasText(request.getIssueUrl())) {
            throw new IllegalArgumentException("issueNumber or issueUrl must be provided");
        }

        String issue = request.getIssueNumber() != null
                ? "#" + request.getIssueNumber()
                : request.getIssueUrl().trim();
        String context = trimContext(request.getContext());

        StringBuilder prompt = new StringBuilder(1_200)
                .append("你是智能研发助手，请分析 GitHub 仓库 ")
                .append(repository)
                .append(" 的 Issue ")
                .append(issue)
                .append("。\n")
                .append("请按以下步骤执行：\n")
                .append("1. 使用可用的 GitHub/MCP 工具获取 Issue、评论和相关代码信息；\n")
                .append("2. 判断问题的影响范围、复现条件和可能根因；\n")
                .append("3. 结合研发知识库检索相关规范或历史解决方案；\n")
                .append("4. 给出可执行的修复方案、风险点和验证步骤；\n")
                .append("5. 输出结论时区分事实、推断和待确认信息，并引用工具或知识库证据。\n");

        if (StringUtils.hasText(context)) {
            prompt.append("\n用户补充背景：\n---\n")
                    .append(context)
                    .append("\n---\n");
        }
        return prompt.toString();
    }

    private static String required(String value, String field) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }

    private static String trimContext(String context) {
        if (!StringUtils.hasText(context)) {
            return "";
        }
        String normalized = context.trim();
        return normalized.length() <= MAX_CONTEXT_CHARS
                ? normalized
                : normalized.substring(0, MAX_CONTEXT_CHARS) + "\n[context truncated]";
    }
}
