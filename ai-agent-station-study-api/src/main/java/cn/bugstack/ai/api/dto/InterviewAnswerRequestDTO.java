package cn.bugstack.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/** Candidate answer submitted during a mock interview. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewAnswerRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** One-based question number. */
    private Integer questionIndex;

    /** The question shown to the candidate. */
    private String question;

    /** Candidate's answer. */
    private String answer;

    /** Optional focus that the interviewer should use while grading. */
    private String expectedFocus;

    private String aiAgentId;

    private String userId;

    private String tenantId;
}
