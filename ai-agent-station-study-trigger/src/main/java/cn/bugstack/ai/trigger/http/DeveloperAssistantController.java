package cn.bugstack.ai.trigger.http;

import cn.bugstack.ai.api.IAiAgentService;
import cn.bugstack.ai.api.dto.AutoAgentRequestDTO;
import cn.bugstack.ai.api.dto.IssueAnalysisRequestDTO;
import cn.bugstack.ai.trigger.developer.DeveloperAssistantPromptBuilder;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 智能研发助手业务入口。
 *
 * <p>该入口只负责研发场景参数校验和指令组装，Agent 的路由、执行、工具治理、
 * 记忆与 SSE 输出统一复用 {@link IAiAgentService}，保持业务入口与执行引擎解耦。</p>
 */
@RestController
@RequestMapping("/api/v1/developer-assistant")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class DeveloperAssistantController {

    @Resource
    private IAiAgentService aiAgentService;

    @PostMapping(value = "/issues/analyze", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseBodyEmitter analyzeIssue(@RequestBody IssueAnalysisRequestDTO request,
                                            HttpServletResponse response) {
        if (request == null) {
            throw new IllegalArgumentException("request must not be null");
        }

        String prompt = DeveloperAssistantPromptBuilder.buildIssueAnalysisPrompt(request);
        AutoAgentRequestDTO delegate = AutoAgentRequestDTO.builder()
                // 8013 is the default multi-step Flow Agent from the local seed data.
                .aiAgentId(StringUtils.hasText(request.getAiAgentId()) ? request.getAiAgentId().trim() : "8013")
                .message(prompt)
                .sessionId(StringUtils.hasText(request.getSessionId())
                        ? request.getSessionId().trim() : UUID.randomUUID().toString())
                .userId(request.getUserId())
                .tenantId(request.getTenantId())
                .maxStep(6)
                .build();

        return aiAgentService.autoAgent(delegate, response);
    }
}
