package com.rev.revpasswordmanagerp2.dto;

public class UpdateSecurityAnswerRequest {

    private String usernameOrEmail;
    private Long questionId;
    private String answer;

    public UpdateSecurityAnswerRequest() {}

    public UpdateSecurityAnswerRequest(String usernameOrEmail,
                                       Long questionId,
                                       String answer) {
        this.usernameOrEmail = usernameOrEmail;
        this.questionId = questionId;
        this.answer = answer;
    }

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}