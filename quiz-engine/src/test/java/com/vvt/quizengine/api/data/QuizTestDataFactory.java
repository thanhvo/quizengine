package com.vvt.quizengine.api.data;

import com.vvt.quizengine.dto.QuestionDTO;
import com.vvt.quizengine.dto.QuizDTO;
import com.vvt.quizengine.model.Question;
import com.vvt.quizengine.model.Quiz;
import com.vvt.quizengine.model.QuizStatus;
import com.vvt.quizengine.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuizTestDataFactory {
    @Autowired
    private QuizService quizService;

    public Quiz createQuiz(Long userId, QuizDTO quizDTO) throws Exception{
        return quizService.createQuiz(userId, quizDTO);
    }

    public Question addQuestion(Quiz quiz, QuestionDTO questionDTO) {
        return this.quizService.addQuestion(quiz, questionDTO);
    }

    public void publishQuiz(Quiz quiz) {
        quiz.setStatus(QuizStatus.PUBLISHED);
        quizService.update(quiz);
    }

    public void update(Quiz quiz) {
        quizService.update(quiz);
    }

    public List<Long> getCorrectAnswers(Long questionId) {
        return quizService.getCorrectAnswerList(questionId);
    }

    public List<Long> getWrongAnswers(Long questionId) {
        return quizService.getWrongAnswerList(questionId);
    }
}
