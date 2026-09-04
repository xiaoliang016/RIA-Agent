package cn.bugstack.ai.trigger.interview;

import cn.bugstack.ai.api.dto.InterviewSessionRequestDTO;
import cn.bugstack.ai.api.dto.InterviewSessionResponseDTO;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Session profile store for the interview facade. Conversation turns remain
 * in the platform's existing Redis/MySQL memory pipeline; this cache only
 * keeps the interview setup and lifecycle metadata.
 */
@Service
public class InterviewSessionService {

    public static final String DEFAULT_AGENT_ID = "8012";
    private static final int DEFAULT_QUESTION_COUNT = 5;
    private static final int MAX_QUESTION_COUNT = 20;

    private final Cache<String, InterviewSessionResponseDTO> sessions = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterAccess(Duration.ofHours(12))
            .build();

    public InterviewSessionResponseDTO create(InterviewSessionRequestDTO request) {
        if (request == null || !StringUtils.hasText(request.getRole())) {
            throw new IllegalArgumentException("role must not be blank");
        }
        int questionCount = request.getQuestionCount() == null
                ? DEFAULT_QUESTION_COUNT : request.getQuestionCount();
        if (questionCount < 1 || questionCount > MAX_QUESTION_COUNT) {
            throw new IllegalArgumentException("questionCount must be between 1 and " + MAX_QUESTION_COUNT);
        }
        List<String> topics = request.getTopics() == null ? List.of() : request.getTopics().stream()
                .filter(StringUtils::hasText).map(String::trim).distinct().limit(10).toList();
        InterviewSessionResponseDTO session = InterviewSessionResponseDTO.builder()
                .sessionId(UUID.randomUUID().toString())
                .role(request.getRole().trim())
                .experienceLevel(defaultValue(request.getExperienceLevel(), "1-3 年"))
                .interviewType(defaultValue(request.getInterviewType(), "技术面试"))
                .topics(topics)
                .questionCount(questionCount)
                .status("CREATED")
                .createdAt(Instant.now())
                .build();
        sessions.put(session.getSessionId(), session);
        return session;
    }

    public InterviewSessionResponseDTO require(String sessionId) {
        if (!StringUtils.hasText(sessionId)) {
            throw new IllegalArgumentException("sessionId must not be blank");
        }
        InterviewSessionResponseDTO session = sessions.getIfPresent(sessionId.trim());
        if (session == null) {
            throw new IllegalArgumentException("interview session not found or expired");
        }
        return session;
    }

    public InterviewSessionResponseDTO markRunning(String sessionId) {
        InterviewSessionResponseDTO session = require(sessionId);
        session.setStatus("RUNNING");
        return session;
    }

    public InterviewSessionResponseDTO markCompleted(String sessionId) {
        InterviewSessionResponseDTO session = require(sessionId);
        session.setStatus("COMPLETED");
        return session;
    }

    public void remove(String sessionId) {
        if (StringUtils.hasText(sessionId)) sessions.invalidate(sessionId.trim());
    }

    private static String defaultValue(String value, String fallback) {
        return StringUtils.hasText(value) ? value.trim() : fallback;
    }
}
