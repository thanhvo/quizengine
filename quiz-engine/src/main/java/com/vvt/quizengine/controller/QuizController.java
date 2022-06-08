package com.vvt.quizengine.controller;

import com.vvt.quizengine.dto.QuizDTO;
import com.vvt.quizengine.model.Quiz;
import com.vvt.quizengine.model.QuizStatus;
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

@RestController
@RequestMapping("/quizzes")
public class QuizController {
    @Autowired
    private QuizService quizService;

    @PostMapping(value = {""})
    public ResponseEntity<Quiz> create(@AuthenticationPrincipal User user, @RequestBody QuizDTO quizDTO) {
        Quiz quiz = Quiz.builder()
                .userId(user.getId())
                .status(QuizStatus.CREATED)
                .title(quizDTO.getTitle())
                .build();
        quizService.update(quiz);
        return new ResponseEntity<>(quiz, HttpStatus.CREATED);
    }
}
