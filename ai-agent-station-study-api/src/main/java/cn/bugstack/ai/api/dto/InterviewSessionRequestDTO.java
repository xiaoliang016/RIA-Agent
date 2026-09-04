package cn.bugstack.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/** Request for creating an AI mock-interview session. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewSessionRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Target position, for example: Java backend engineer. */
    private String role;

    /** Candidate experience level, for example: 1-3 years. */
    private String experienceLevel;

    /** Interview mode, for example: technical, project, or mixed. */
    private String interviewType;

    /** Topics the interviewer should focus on. */
    private List<String> topics;

    /** Number of questions expected in this session. */
    private Integer questionCount;

    /** Optional preselected Agent; defaults to the general Auto Agent. */
    private String aiAgentId;

    private String userId;

    private String tenantId;
}
