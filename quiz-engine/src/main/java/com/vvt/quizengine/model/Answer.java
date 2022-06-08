package com.vvt.quizengine.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Setter @Getter @NoArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long questionId;

    private String value;

    private Boolean correct;

    @Builder
    public Answer(Long questionId, String value, Boolean correct) {
        this.questionId = questionId;
        this.value = value;
        this.correct = correct;
    }

}
