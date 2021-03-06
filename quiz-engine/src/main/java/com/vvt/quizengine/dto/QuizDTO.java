package com.vvt.quizengine.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @NoArgsConstructor
public class QuizDTO {
    private String title;

    public QuizDTO(String title) {
        this.title = title;
    }
}
