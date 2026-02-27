package com.rev.revpasswordmanagerp2.dto;

import lombok.Data;
import java.util.List;

@Data
public class UpdateAnswersRequest {

    private Long userId;
    private List<AnswerRequest> answers;
}