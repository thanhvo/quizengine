package com.vvt.quizengine.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Entity
@Setter @Getter @NoArgsConstructor
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long questionId;

    private Long solutionId;

    @Column
    @Convert(converter = ListToStringConverter.class)
    private List<Long> answerIds;

    private Double score;

    @Builder
    public Response(Long questionId, Long solutionId, List<Long> answerIds, Double score) {
        this.questionId = questionId;
        this.solutionId = solutionId;
        this.answerIds = answerIds;
        this.score = score;
    }

}
