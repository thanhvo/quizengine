package com.vvt.quizengine.service;

import com.vvt.quizengine.dto.ResponseDTO;
import com.vvt.quizengine.dto.ScoreDTO;
import com.vvt.quizengine.dto.SolutionDTO;
import com.vvt.quizengine.exception.ApiError;
import com.vvt.quizengine.model.Answer;
import com.vvt.quizengine.model.Response;
import com.vvt.quizengine.model.Solution;
import com.vvt.quizengine.repository.AnswerRepository;
import com.vvt.quizengine.repository.ResponseRepository;
import com.vvt.quizengine.repository.SolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SolutionServiceImpl implements SolutionService{

    @Autowired
    private SolutionRepository solutionRepository;

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuizService quizService;

    @Override
    public List<Solution> getSolutions(Long userId) {
        return solutionRepository.getSolutions(userId);
    }

    @Override
    public List<Solution> getSolutionsByQuizId(Long quizId) {
        return solutionRepository.getSolutionsByQuizId(quizId);
    }

    @Override
    public Solution createSolution(Long userId, SolutionDTO solutionDto) throws Exception {
        Solution solution = Solution.builder()
                .userId(userId)
                .quizId(solutionDto.getQuizId())
                .build();
        solution = update(solution);

        List<Response> responses = new ArrayList<Response>();
        Double totalScore = 0.0;
        for (ResponseDTO responseDto : solutionDto.getResponses()) {
            Response response = Response.builder()
                    .solutionId(solution.getId())
                    .questionId(responseDto.getQuestionId())
                    .answerIds(responseDto.getAnswerIds())
                    .build();

            Double score = null;
            try {
                score = caculateScore(response);
            } catch (Exception e) {
                throw e;
            }
            totalScore += score;
            response.setScore(score);
            response = update(response);
            responses.add(response);
        }

        int percentage = (int)(totalScore / this.quizService.getQuestions(solutionDto.getQuizId()) * 100);
        solution.setTotalScore(percentage);
        solution.setReponses(responses);
        solution = update(solution);
        return solution;
    }

    @Override
    public Solution update(Solution solution) {
        return solutionRepository.save(solution);
    }

    @Override
    public Response update(Response response) {
        return responseRepository.save(response);
    }

    @Override
    public Double caculateScore(Response response) throws Exception {
        int correctReponses = 0, wrongResponses = 0;
        int wrongAnswers = this.quizService.getWrongAnswers(response.getQuestionId()).intValue();
        int correctAnswers = (int)this.quizService.getCorrectAnswers(response.getQuestionId()).intValue();
        for (Long answerId: response.getAnswerIds() ) {
            Answer answer = this.answerRepository.findById(answerId)
                    .orElseThrow(() -> new Exception("Answer not found!"));
            if (answer.getCorrect()) {
                correctReponses++;
            } else {
                wrongResponses++;
            }
        }
        return (double) correctReponses / correctAnswers - (double) wrongResponses / wrongAnswers;
    }

}
