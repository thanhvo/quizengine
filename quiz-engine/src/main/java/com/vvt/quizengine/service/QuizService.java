package com.vvt.quizengine.service;

import com.vvt.quizengine.model.Quiz;
import com.vvt.quizengine.model.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface QuizService {
    public Quiz getQuiz(long id) throws Exception;

    public Quiz update(Quiz quiz);
}
