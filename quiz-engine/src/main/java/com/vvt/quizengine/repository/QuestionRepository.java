package com.vvt.quizengine.repository;

import com.vvt.quizengine.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
