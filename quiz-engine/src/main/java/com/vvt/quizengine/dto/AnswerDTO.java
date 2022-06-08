package com.vvt.quizengine.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @NoArgsConstructor
public class AnswerDTO {

    private String value;

    private Boolean correct;
}
