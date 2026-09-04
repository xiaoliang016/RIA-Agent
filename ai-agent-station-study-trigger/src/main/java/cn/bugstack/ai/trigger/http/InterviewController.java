package cn.bugstack.ai.trigger.http;

import cn.bugstack.ai.api.IAiAgentService;
import cn.bugstack.ai.api.dto.AutoAgentRequestDTO;
import cn.bugstack.ai.api.dto.InterviewAnswerRequestDTO;
import cn.bugstack.ai.api.dto.InterviewSessionRequestDTO;
import cn.bugstack.ai.api.dto.InterviewSessionResponseDTO;
import cn.bugstack.ai.api.response.Response;
import cn.bugstack.ai.trigger.interview.InterviewPromptBuilder;
import cn.bugstack.ai.trigger.interview.InterviewSessionService;
import cn.bugstack.ai.types.enums.ResponseCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/** HTTP facade for the AI mock-interview and career-coaching scenario. */
@Slf4j
@RestController
@RequestMapping("/api/v1/interview")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS
})
public class InterviewController {

    @Resource
    private IAiAgentService aiAgentService;

    @Resource
    private InterviewSessionService sessionService;

    @PostMapping("/sessions")
    public Response<InterviewSessionResponseDTO> createSession(@RequestBody InterviewSessionRequestDTO request) {
        try {
            return success(sessionService.create(request));
        } catch (IllegalArgumentException e) {
            return failure(ResponseCode.ILLEGAL_PARAMETER, e.getMessage());
        } catch (Exception e) {
            log.error("Create interview session failed", e);
            return failure(ResponseCode.UN_ERROR, "创建面试会话失败");
        }
    }

    @GetMapping("/sessions/{sessionId}")
    public Response<InterviewSessionResponseDTO> getSession(@PathVariable String sessionId) {
        try {
            return success(sessionService.require(sessionId));
        } catch (IllegalArgumentException e) {
            return failure(ResponseCode.ILLEGAL_PARAMETER, e.getMessage());
        }
    }

    @PostMapping(value = "/sessions/{sessionId}/start", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseBodyEmitter start(@PathVariable String sessionId,
                                     @RequestHeader(value = "X-User-Id", required = false) String userId,
                                     @RequestHeader(value = "X-Tenant-Id", required = false) String tenantId,
                                     HttpServletResponse response) {
        InterviewSessionResponseDTO session = sessionService.markRunning(sessionId);
        return dispatch(session, InterviewPromptBuilder.buildOpeningPrompt(session),
                userId, tenantId, response, 3);
    }

    @PostMapping(value = "/sessions/{sessionId}/answers", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseBodyEmitter answer(@PathVariable String sessionId,
                                      @RequestBody InterviewAnswerRequestDTO request,
                                      @RequestHeader(value = "X-User-Id", required = false) String headerUserId,
                                      @RequestHeader(value = "X-Tenant-Id", required = false) String headerTenantId,
                                      HttpServletResponse response) {
        InterviewSessionResponseDTO session = sessionService.require(sessionId);
        if (request == null) throw new IllegalArgumentException("answer request must not be null");
        String userId = firstNonBlank(request.getUserId(), headerUserId);
        String tenantId = firstNonBlank(request.getTenantId(), headerTenantId);
        return dispatch(session, InterviewPromptBuilder.buildAnswerPrompt(session, request),
                userId, tenantId, response, 4);
    }

    @PostMapping(value = "/sessions/{sessionId}/report", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseBodyEmitter report(@PathVariable String sessionId,
                                      @RequestHeader(value = "X-User-Id", required = false) String userId,
                                      @RequestHeader(value = "X-Tenant-Id", required = false) String tenantId,
                                      HttpServletResponse response) {
        InterviewSessionResponseDTO session = sessionService.markCompleted(sessionId);
        return dispatch(session, InterviewPromptBuilder.buildReportPrompt(session),
                userId, tenantId, response, 4);
    }

    @DeleteMapping("/sessions/{sessionId}")
    public Response<Boolean> delete(@PathVariable String sessionId) {
        sessionService.remove(sessionId);
        return success(true);
    }

    @GetMapping("/topics")
    public Response<List<Map<String, Object>>> topics() {
        return success(List.of(
                Map.of("id", "java", "name", "Java 基础"),
                Map.of("id", "spring", "name", "Spring / Spring Boot"),
                Map.of("id", "database", "name", "MySQL / PostgreSQL"),
                Map.of("id", "middleware", "name", "Redis / 消息队列"),
                Map.of("id", "jvm", "name", "JVM 与性能调优"),
                Map.of("id", "architecture", "name", "分布式系统与架构"),
                Map.of("id", "project", "name", "项目实战与系统设计")
        ));
    }

    private ResponseBodyEmitter dispatch(InterviewSessionResponseDTO session, String prompt,
                                          String userId, String tenantId, HttpServletResponse response,
                                          int maxStep) {
        AutoAgentRequestDTO delegate = AutoAgentRequestDTO.builder()
                .aiAgentId(InterviewSessionService.DEFAULT_AGENT_ID)
                .message(prompt)
                .sessionId(session.getSessionId())
                .userId(userId)
                .tenantId(tenantId)
                .maxStep(maxStep)
                .build();
        return aiAgentService.autoAgent(delegate, response);
    }

    private static String firstNonBlank(String first, String second) {
        return StringUtils.hasText(first) ? first.trim()
                : (StringUtils.hasText(second) ? second.trim() : null);
    }

    private static <T> Response<T> success(T data) {
        return Response.<T>builder().code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo()).data(data).build();
    }

    private static <T> Response<T> failure(ResponseCode code, String message) {
        return Response.<T>builder().code(code.getCode())
                .info(StringUtils.hasText(message) ? message : code.getInfo()).build();
    }
}
