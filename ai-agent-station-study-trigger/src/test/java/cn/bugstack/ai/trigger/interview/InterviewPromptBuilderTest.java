package cn.bugstack.ai.trigger.interview;

import cn.bugstack.ai.api.dto.InterviewAnswerRequestDTO;
import cn.bugstack.ai.api.dto.InterviewSessionResponseDTO;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InterviewPromptBuilderTest {

    private final InterviewSessionResponseDTO session = InterviewSessionResponseDTO.builder()
            .sessionId("session-1")
            .role("Java 后端开发")
            .experienceLevel("1-3 年")
            .interviewType("技术面试")
            .topics(List.of("Spring Boot", "MySQL"))
            .questionCount(5)
            .status("CREATED")
            .createdAt(Instant.now())
            .build();

    @Test
    void openingPromptContainsInterviewContext() {
        String prompt = InterviewPromptBuilder.buildOpeningPrompt(session);

        assertTrue(prompt.contains("Java 后端开发"));
        assertTrue(prompt.contains("Spring Boot、MySQL"));
        assertTrue(prompt.contains("第 1 道问题"));
    }

    @Test
    void answerPromptContainsScoreRubricAndTruncatesHugeAnswer() {
        String prompt = InterviewPromptBuilder.buildAnswerPrompt(session,
                InterviewAnswerRequestDTO.builder()
                        .questionIndex(2)
                        .question("请解释 Spring 事务传播机制")
                        .answer("a".repeat(9_000))
                        .build());

        assertTrue(prompt.contains("第 2 题"));
        assertTrue(prompt.contains("技术准确性、完整性、表达清晰度、实战深度"));
        assertTrue(prompt.contains("[answer truncated]"));
    }

    @Test
    void rejectsBlankCandidateAnswer() {
        assertThrows(IllegalArgumentException.class, () -> InterviewPromptBuilder.buildAnswerPrompt(session,
                InterviewAnswerRequestDTO.builder()
                        .question("问题")
                        .answer(" ")
                        .build()));
    }
}
