package com.vvt.quizengine.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter @Getter
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private QuizStatus status;

    private String title;

    private Long userId;

    private String encodedUrl;

    @OneToMany(mappedBy = "quizId", fetch = FetchType.EAGER)
    private List<Question> questions;

    public Quiz() {
        questions = new ArrayList<Question>();
    }

    @Builder
    public Quiz(Long userId, QuizStatus status, String title) {
        this.userId = userId;
        this.status = status;
        this.title = title;
        this.questions = new ArrayList<Question>();
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }
}
