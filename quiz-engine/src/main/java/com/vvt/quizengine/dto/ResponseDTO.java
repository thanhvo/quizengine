package com.vvt.quizengine.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter @Getter @NoArgsConstructor
public class ResponseDTO {
    private Long questionId;

    private List<Long> answerIds;

    public ResponseDTO(Long questionId, List<Long> answerIds) {
        this.questionId = questionId;
        this.answerIds = answerIds;
    }
}
