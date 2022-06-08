package com.vvt.quizengine.controller;

import com.vvt.quizengine.dto.AnswerDTO;
import com.vvt.quizengine.dto.QuestionDTO;
import com.vvt.quizengine.dto.QuizDTO;
import com.vvt.quizengine.model.Answer;
import com.vvt.quizengine.model.Question;
import com.vvt.quizengine.model.Quiz;
import com.vvt.quizengine.model.QuizStatus;
import com.vvt.quizengine.model.User;
import com.vvt.quizengine.service.QuizService;
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
        this.quizService.update(quiz);
        return new ResponseEntity<>(quiz, HttpStatus.CREATED);
    }

    @PostMapping(value = {"/addQuestion"})
    public ResponseEntity<Question> addQuestion(@AuthenticationPrincipal User user, @RequestBody QuestionDTO questionDTO) throws Exception {
        Quiz quiz = this.quizService.getQuiz(questionDTO.getQuizId());
        if (quiz.getUserId() != user.getId()) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        quiz.setStatus(QuizStatus.MODIFIED);

        Question question = Question.builder()
                .quizId(questionDTO.getQuizId())
                .text(questionDTO.getText())
                .type(questionDTO.getType())
                .build();
        question = this.quizService.update(question);
        List<Answer> answers = new ArrayList<Answer>();
        for (AnswerDTO answerDto: questionDTO.getAnswers()) {
            Answer answer = Answer.builder()
                    .questionId(question.getId())
                    .value(answerDto.getValue())
                    .correct(answerDto.getCorrect())
                    .build();
            answer = this.quizService.update(answer);
            answers.add(answer);
        }
        question.setAnswers(answers);
        question = this.quizService.update(question);

        quiz.addQuestion(question);
        this.quizService.update(quiz);

        return new ResponseEntity<>(question, HttpStatus.CREATED);
    }

    @GetMapping(value = {"/{id}"})
    public ResponseEntity<Quiz> getQuiz(@AuthenticationPrincipal User user, @PathVariable Long id) throws Exception {
        Quiz quiz = this.quizService.getQuiz(id);
        if (quiz.getStatus() != QuizStatus.PUBLISHED && user.getId() != quiz.getUserId()) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    @PostMapping(value = {"/publish/{id}"})
    public ResponseEntity<Quiz> publish(@AuthenticationPrincipal User user, @PathVariable Long id) throws Exception {
        Quiz quiz = this.quizService.getQuiz(id);
        if (user.getId() != quiz.getUserId()) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        quiz.setStatus(QuizStatus.PUBLISHED);
        quiz = this.quizService.update(quiz);
        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }
}
