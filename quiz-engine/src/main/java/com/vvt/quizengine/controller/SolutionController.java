package com.vvt.quizengine.controller;

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
import com.vvt.quizengine.service.SolutionService;
import com.vvt.quizengine.utils.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/solutions")
public class SolutionController {

    @Autowired
    private SolutionService solutionService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private URLEncoder urlEncoder;

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
        Solution solution = null;
        try {
            solution = solutionService.createSolution(user.getId(), solutionDTO);
        } catch (Exception ex) {
            ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
            return new ResponseEntity<>(error, error.getStatus());
        }
        ScoreDTO scoreDto = new ScoreDTO(solution.getTotalScore() + "%",
                solution.getReponses().stream().map(response -> response.getScore())
                        .collect(Collectors.toList())
        );
        return new ResponseEntity<>(scoreDto, HttpStatus.CREATED);
    }

    @GetMapping(value = {"", "/"})
    public Iterable<Solution> getSolutions(@AuthenticationPrincipal User user) {
        return this.solutionService.getSolutions(user.getId());
    }

    @GetMapping(value = {"/byQuiz/{encodedUrl}"})
    public ResponseEntity<Object> getSolutionsByQuizId(@AuthenticationPrincipal User user, @PathVariable String encodedUrl) {
        Long id = null;
        Quiz quiz = null;
        try {
            id = Long.valueOf(urlEncoder.decode(encodedUrl));
            quiz = this.quizService.getQuiz(id);
        } catch (Exception e) {
            ApiError error = new ApiError(HttpStatus.BAD_REQUEST, "Can not find the quiz.");
            return new ResponseEntity<>(error, error.getStatus());
        }
        if (user.getId() != quiz.getUserId()) {
            ApiError error = new ApiError(HttpStatus.FORBIDDEN, "Can not access the solution of the quiz owned by other.");
            return new ResponseEntity<>(error, error.getStatus());
        }
        List<Solution> solutions = this.solutionService.getSolutionsByQuizId(id);
        return new ResponseEntity<>(solutions, HttpStatus.OK);
    }
}
