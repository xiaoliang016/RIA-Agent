package cn.bugstack.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/** Public view of an interview session. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewSessionResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String sessionId;
    private String role;
    private String experienceLevel;
    private String interviewType;
    private List<String> topics;
    private Integer questionCount;
    private String status;
    private Instant createdAt;
}
