package com.vvt.quizengine.service;

import com.vvt.quizengine.dto.QuestionDTO;
import com.vvt.quizengine.dto.QuizDTO;
import com.vvt.quizengine.model.Answer;
import com.vvt.quizengine.model.Question;
import com.vvt.quizengine.model.Quiz;
import com.vvt.quizengine.model.Response;
import com.vvt.quizengine.model.Solution;
import com.vvt.quizengine.model.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface QuizService {
    public Quiz createQuiz(Long userId, QuizDTO quizDTO) throws Exception;

    public Quiz getQuiz(long id) throws Exception;

    public Quiz update(Quiz quiz);

    public Question update(Question question);

    public Answer update(Answer answer);

    public void deleteQuiz(Long id);

    public Long getQuestions(Long quizId);

    public Question addQuestion(Quiz quiz, QuestionDTO questionDTO);

    public Long getWrongAnswers(Long questionId);

    public Long getCorrectAnswers(Long questionId);

    public List<Long> getCorrectAnswerList(Long questionId);

    public List<Long> getWrongAnswerList(Long questionId);
}
