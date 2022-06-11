package com.vvt.quizengine.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @NoArgsConstructor
public class AnswerDTO {

    private String value;

    private Boolean correct;

    @Builder
    public AnswerDTO(String value, Boolean correct) {
        this.value = value;
        this.correct = correct;
    }
}
