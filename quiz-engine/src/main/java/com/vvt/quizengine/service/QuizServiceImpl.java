package com.vvt.quizengine.service;

import com.vvt.quizengine.model.Answer;
import com.vvt.quizengine.model.Question;
import com.vvt.quizengine.model.Quiz;
import com.vvt.quizengine.repository.AnswerRepository;
import com.vvt.quizengine.repository.QuestionRepository;
import com.vvt.quizengine.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizServiceImpl implements QuizService{
    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Override
    public Quiz getQuiz(long id) throws Exception {
        return quizRepository
                .findById(id)
                .orElseThrow(() -> new Exception("Quiz not found!"));
    }

    @Override
    public Quiz update(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @Override
    public Question update(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public Answer update(Answer answer) {
        return answerRepository.save(answer);
    }

}
