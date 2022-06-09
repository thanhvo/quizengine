package com.vvt.quizengine.repository;

import com.vvt.quizengine.model.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SolutionRepository extends JpaRepository<Solution, Long> {
    @Query("SELECT s FROM Solution s WHERE s.userId = :userId")
    public List<Solution> getSolutions(Long userId);

    @Query("SELECT s FROM Solution s WHERE s.quizId = :quizId")
    public List<Solution> getSolutionsByQuizId(Long quizId);
}
