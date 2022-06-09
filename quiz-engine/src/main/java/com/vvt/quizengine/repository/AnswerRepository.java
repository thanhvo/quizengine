package com.vvt.quizengine.repository;

import com.vvt.quizengine.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query("SELECT count(a) FROM Answer a WHERE a.questionId = :questionId AND a.correct = true")
    public Long getCorrectAnswers(Long questionId);

    @Query("SELECT count(a) FROM Answer a WHERE a.questionId = :questionId AND a.correct = false")
    public Long getWrongAnswers(Long questionId);
}
