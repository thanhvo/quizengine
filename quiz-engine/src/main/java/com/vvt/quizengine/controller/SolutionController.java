package com.vvt.quizengine.controller;

import com.vvt.quizengine.dto.QuizDTO;
import com.vvt.quizengine.dto.ResponseDTO;
import com.vvt.quizengine.dto.ScoreDTO;
import com.vvt.quizengine.dto.SolutionDTO;
import com.vvt.quizengine.exception.ApiError;
import com.vvt.quizengine.model.Quiz;
import com.vvt.quizengine.model.QuizStatus;
import com.vvt.quizengine.model.Response;
import com.vvt.quizengine.model.Solution;
import com.vvt.quizengine.model.User;
import com.vvt.quizengine.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/solutions")
public class SolutionController {

    @Autowired
    QuizService quizService;

    @PostMapping(value = {""})
    public ResponseEntity<Object> create(@AuthenticationPrincipal User user, @RequestBody SolutionDTO solutionDTO) {
        Quiz quiz = null;
        try {
            quiz = this.quizService.getQuiz(solutionDTO.getQuizId());
        } catch (Exception e) {
            ApiError error = new ApiError(HttpStatus.BAD_REQUEST, "Can not find the quiz.");
            return new ResponseEntity<>(error, error.getStatus());
        }
        if (quiz.getStatus() != QuizStatus.PUBLISHED) {
            ApiError error = new ApiError(HttpStatus.FORBIDDEN, "Can take the quiz which is not yet published.");
            return new ResponseEntity<Object>(error, error.getStatus());
        }

        Solution solution = Solution.builder()
                .userId(user.getId())
                .quizId(quiz.getId())
                .build();
        solution = this.quizService.update(solution);

        List<Response> responses = new ArrayList<Response>();
        Double totalScore = 0.0;
        List<Double> subScores = new ArrayList<>();
        for (ResponseDTO responseDto : solutionDTO.getResponses()) {
            Response response = Response.builder()
                    .solutionId(solution.getId())
                    .questionId(responseDto.getQuestionId())
                    .answerIds(responseDto.getAnswerIds())
                    .build();

            Double score = this.quizService.caculateScore(response);
            subScores.add(score);
            totalScore += score;
            response = this.quizService.update(response);
            responses.add(response);
        }
        solution.setTotalScore(totalScore);
        this.quizService.update(solution);
        ScoreDTO scoreDto = new ScoreDTO(totalScore, subScores);
        return new ResponseEntity<>(scoreDto, HttpStatus.CREATED);
    }
}
