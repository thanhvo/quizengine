package com.vvt.quizengine.dto;

import com.vvt.quizengine.model.Answer;
import com.vvt.quizengine.model.QuestionType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter @Getter @NoArgsConstructor
public class QuestionDTO {
    private Long quizId;

    private String text;

    private QuestionType type;

    private List<AnswerDTO> answers;

    @Builder
    public QuestionDTO(Long quizId, String text, QuestionType type) {
        this.quizId = quizId;
        this.text = text;
        this.type = type;
    }

    public void addAnswer(AnswerDTO answer) {
        if (answers == null) answers = new ArrayList<>();
        answers.add(answer);
    }
}
