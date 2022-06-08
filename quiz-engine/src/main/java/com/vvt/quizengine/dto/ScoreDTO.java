package com.vvt.quizengine.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter @Getter @NoArgsConstructor
public class ScoreDTO {
    private Double totalScore;

    private List<Double> subScores;

    public ScoreDTO(Double totalScore, List<Double> subScores) {
        this.totalScore = totalScore;
        this.subScores = subScores;
    }
}
