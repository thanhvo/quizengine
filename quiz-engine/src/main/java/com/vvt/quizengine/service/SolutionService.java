package com.vvt.quizengine.service;

import com.vvt.quizengine.dto.ScoreDTO;
import com.vvt.quizengine.dto.SolutionDTO;
import com.vvt.quizengine.model.Response;
import com.vvt.quizengine.model.Solution;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Map;

public interface SolutionService {
    public Solution update(Solution solution);

    public Response update(Response response);

    public Double caculateScore(Response response) throws Exception;

    public List<Solution> getSolutions(Long userId);

    public List<Solution> getSolutionsByQuizId(Long quizId);

    public Solution createSolution(Long userId, SolutionDTO solutionDto) throws Exception;

}
