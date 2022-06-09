package com.vvt.quizengine.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Setter @Getter @NoArgsConstructor
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long quizId;

    private Integer totalScore;

    @OneToMany(mappedBy = "solutionId")
    private List<Response> reponses;

    @Builder
    public Solution(Long userId, Long quizId, Integer totalScore) {
        this.userId = userId;
        this.quizId = quizId;
        this.totalScore = totalScore;
    }

}
