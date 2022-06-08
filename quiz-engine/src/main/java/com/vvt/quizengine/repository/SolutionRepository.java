package com.vvt.quizengine.repository;

import com.vvt.quizengine.model.Solution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolutionRepository extends JpaRepository<Solution, Long> {
}
