package com.vvt.quizengine.dto;

import com.vvt.quizengine.model.Answer;
import com.vvt.quizengine.model.QuestionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter @Getter @NoArgsConstructor
public class QuestionDTO {
    private Long quizId;

    private String text;

    private QuestionType type;

    private List<AnswerDTO> answers;
}
