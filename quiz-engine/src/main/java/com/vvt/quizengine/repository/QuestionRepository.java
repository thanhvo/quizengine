package com.vvt.quizengine.repository;

import com.vvt.quizengine.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("SELECT count(q) FROM Question q WHERE q.quizId = :quizId")
    public Long getQuestions(Long quizId);
}
