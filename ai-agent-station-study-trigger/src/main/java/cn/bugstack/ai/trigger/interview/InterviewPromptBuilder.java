package cn.bugstack.ai.trigger.interview;

import cn.bugstack.ai.api.dto.InterviewAnswerRequestDTO;
import cn.bugstack.ai.api.dto.InterviewSessionResponseDTO;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

/** Builds focused prompts for the mock-interview use case. */
public final class InterviewPromptBuilder {

    private static final int MAX_ANSWER_CHARS = 8_000;

    private InterviewPromptBuilder() {
    }

    public static String buildOpeningPrompt(InterviewSessionResponseDTO session) {
        String topics = session.getTopics() == null || session.getTopics().isEmpty()
                ? "Java 后端通用能力"
                : session.getTopics().stream().filter(StringUtils::hasText).collect(Collectors.joining("、"));
        return "你是专业的 AI 技术面试官，现在开始一场模拟面试。\n"
                + "岗位：" + session.getRole() + "\n"
                + "候选人经验：" + session.getExperienceLevel() + "\n"
                + "面试类型：" + session.getInterviewType() + "\n"
                + "重点方向：" + topics + "\n"
                + "预计题目数：" + session.getQuestionCount() + "\n\n"
                + "请只提出第 1 道问题，不要提前给出答案。问题要贴合岗位和重点方向，"
                + "并要求候选人结合实际项目说明。如果是项目题，请追问技术选型和取舍。"
                + "使用 Markdown 输出，标题为“第 1 题”。";
    }

    public static String buildAnswerPrompt(InterviewSessionResponseDTO session, InterviewAnswerRequestDTO answer) {
        String question = required(answer.getQuestion(), "question");
        String candidateAnswer = required(answer.getAnswer(), "answer");
        if (candidateAnswer.length() > MAX_ANSWER_CHARS) {
            candidateAnswer = candidateAnswer.substring(0, MAX_ANSWER_CHARS) + "\n[answer truncated]";
        }
        int index = answer.getQuestionIndex() == null || answer.getQuestionIndex() < 1
                ? 1 : answer.getQuestionIndex();
        String focus = StringUtils.hasText(answer.getExpectedFocus())
                ? answer.getExpectedFocus().trim() : "结合岗位要求判断技术正确性和实战深度";

        return "你是正在进行模拟面试的 AI 技术面试官。请评估候选人第 " + index + " 题的回答，并继续面试。\n"
                + "岗位：" + session.getRole() + "；候选人经验：" + session.getExperienceLevel() + "\n"
                + "考察重点：" + focus + "\n\n"
                + "题目：\n---\n" + question + "\n---\n"
                + "候选人回答：\n---\n" + candidateAnswer + "\n---\n\n"
                + "请严格按以下结构输出：\n"
                + "1. 本题评分：技术准确性、完整性、表达清晰度、实战深度四项，每项 0-25 分，并给出总分；\n"
                + "2. 面试官反馈：指出回答中做得好的地方和需要改进的地方，给出正确思路或示例；\n"
                + "3. 下一题：只提出一道递进问题，不要同时给出答案；如果已达到预计题目数，说明可以生成面试报告。";
    }

    public static String buildReportPrompt(InterviewSessionResponseDTO session) {
        return "请基于当前会话中的全部模拟面试问答，生成一份面向候选人的面试总结报告。\n"
                + "岗位：" + session.getRole() + "；候选人经验：" + session.getExperienceLevel() + "\n"
                + "重点方向：" + (session.getTopics() == null ? "" : String.join("、", session.getTopics())) + "\n\n"
                + "报告必须包含：\n"
                + "- 综合得分（0-100）和各能力维度得分；\n"
                + "- 技术优势、知识薄弱点和回答中的典型问题；\n"
                + "- 按优先级排序的学习计划，包含可执行的练习建议；\n"
                + "- 是否建议进入下一轮面试以及理由。\n"
                + "只输出报告，不要继续提问。";
    }

    private static String required(String value, String field) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }
}
