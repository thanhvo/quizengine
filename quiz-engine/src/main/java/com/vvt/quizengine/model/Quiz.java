package com.vvt.quizengine.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;

@Entity
@Setter @Getter @NoArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateCreated;

    private QuizStatus status;

    private String title;

    private Long userId;

    @OneToMany(mappedBy = "quizId")
    private List<Question> questions;

    @Builder
    public Quiz(Long userId, QuizStatus status, String title) {
        this.userId = userId;
        this.status = status;
        this.title = title;
    }
}
