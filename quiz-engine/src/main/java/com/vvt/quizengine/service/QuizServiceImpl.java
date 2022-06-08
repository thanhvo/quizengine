package com.vvt.quizengine.service;

import com.vvt.quizengine.model.Quiz;
import com.vvt.quizengine.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizServiceImpl implements QuizService{
    @Autowired
    private QuizRepository quizRepository;

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
}
