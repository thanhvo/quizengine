package com.vvt.quizengine.repository;

import com.vvt.quizengine.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
