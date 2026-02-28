package com.rev.revpasswordmanagerp2.dto;

import lombok.Data;

@Data
public class AnswerRequest {

    private Long questionId;
    private String answer;
}