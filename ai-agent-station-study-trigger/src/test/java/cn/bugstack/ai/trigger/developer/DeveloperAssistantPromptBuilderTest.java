package cn.bugstack.ai.trigger.developer;

import cn.bugstack.ai.api.dto.IssueAnalysisRequestDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeveloperAssistantPromptBuilderTest {

    @Test
    void buildsStructuredIssuePrompt() {
        String prompt = DeveloperAssistantPromptBuilder.buildIssueAnalysisPrompt(
                IssueAnalysisRequestDTO.builder()
                        .repository("xiaoliang016/123")
                        .issueNumber(42L)
                        .context("接口返回 503")
                        .build());

        assertTrue(prompt.contains("xiaoliang016/123"));
        assertTrue(prompt.contains("#42"));
        assertTrue(prompt.contains("接口返回 503"));
        assertTrue(prompt.contains("修复方案"));
    }

    @Test
    void rejectsRequestWithoutRepositoryOrIssue() {
        assertThrows(IllegalArgumentException.class, () ->
                DeveloperAssistantPromptBuilder.buildIssueAnalysisPrompt(
                        IssueAnalysisRequestDTO.builder().issueNumber(1L).build()));
        assertThrows(IllegalArgumentException.class, () ->
                DeveloperAssistantPromptBuilder.buildIssueAnalysisPrompt(
                        IssueAnalysisRequestDTO.builder().repository("owner/repo").build()));
    }
}
