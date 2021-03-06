package com.vvt.quizengine.controller;

import com.vvt.quizengine.dto.AnswerDTO;
import com.vvt.quizengine.dto.QuestionDTO;
import com.vvt.quizengine.dto.QuizDTO;
import com.vvt.quizengine.exception.ApiError;
import com.vvt.quizengine.model.Answer;
import com.vvt.quizengine.model.Question;
import com.vvt.quizengine.model.Quiz;
import com.vvt.quizengine.model.QuizStatus;
import com.vvt.quizengine.model.User;
import com.vvt.quizengine.service.QuizService;
import com.vvt.quizengine.utils.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/quizzes")
public class QuizController {
    @Autowired
    private QuizService quizService;

    @Autowired
    private URLEncoder urlEncoder;

    @PostMapping(value = {""})
    public ResponseEntity<Object> create(@AuthenticationPrincipal User user, @RequestBody QuizDTO quizDTO) {
        Quiz quiz = null;
        try {
            quiz = this.quizService.createQuiz(user.getId(), quizDTO);
        } catch (Exception e) {
            ApiError error = new ApiError(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(error, error.getStatus());
        }
        return new ResponseEntity<>(quiz, HttpStatus.CREATED);
    }

    @PostMapping(value = {"/addQuestion"})
    public ResponseEntity<Object> addQuestion(@AuthenticationPrincipal User user, @RequestBody QuestionDTO questionDTO) {
        Quiz quiz = null;
        try {
            quiz = this.quizService.getQuiz(questionDTO.getQuizId());
        } catch (Exception e) {
            ApiError error = new ApiError(HttpStatus.BAD_REQUEST, "Can not find the quiz.");
            return new ResponseEntity<>(error, error.getStatus());
        }
        if (quiz.getUserId() != user.getId()) {
            ApiError error = new ApiError(HttpStatus.FORBIDDEN, "Can not modify the quiz which the user does not own.");
            return new ResponseEntity<Object>(error, error.getStatus());
        } else if (quiz.getStatus() == QuizStatus.PUBLISHED) {
            ApiError error = new ApiError(HttpStatus.FORBIDDEN, "Can not modify the quiz which is already published.");
            return new ResponseEntity<Object>(error, error.getStatus());
        }
        quiz.setStatus(QuizStatus.MODIFIED);
        Question question = quizService.addQuestion(quiz, questionDTO);
        return new ResponseEntity<>(question, HttpStatus.CREATED);
    }

    @GetMapping(value = {"/{encodedUrl}"})
    public ResponseEntity<Object> getQuiz(@AuthenticationPrincipal User user, @PathVariable String encodedUrl) {
        Long id = null;
        Quiz quiz = null;
        try {
            id = Long.valueOf(urlEncoder.decode(encodedUrl));
            quiz = this.quizService.getQuiz(id);
        } catch (Exception e) {
            ApiError error = new ApiError(HttpStatus.BAD_REQUEST, "Can not find the quiz.");
            return new ResponseEntity<>(error, error.getStatus());
        }
        if (quiz.getStatus() != QuizStatus.PUBLISHED && user.getId() != quiz.getUserId()) {
            ApiError error = new ApiError(HttpStatus.FORBIDDEN, "Can not take the quiz which is not published.");
            return new ResponseEntity<Object>(error, error.getStatus());
        }
        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    @PostMapping(value = {"/publish/{encodedUrl}"})
    public ResponseEntity<Object> publish(@AuthenticationPrincipal User user, @PathVariable String encodedUrl) {
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
            ApiError error = new ApiError(HttpStatus.FORBIDDEN, "Can not publish another user's quiz.");
            return new ResponseEntity<Object>(error, error.getStatus());
        }
        quiz.setStatus(QuizStatus.PUBLISHED);
        quiz = this.quizService.update(quiz);
        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    @DeleteMapping(value = {"/{encodedUrl}"})
    public ResponseEntity<Object> delete(@AuthenticationPrincipal User user, @PathVariable String encodedUrl) {
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
            ApiError error = new ApiError(HttpStatus.FORBIDDEN, "Can not delete another user's quiz.");
            return new ResponseEntity<Object>(error, error.getStatus());
        }

        this.quizService.deleteQuiz(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
