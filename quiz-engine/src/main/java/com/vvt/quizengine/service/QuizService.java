package com.vvt.quizengine.service;

import com.vvt.quizengine.model.Answer;
import com.vvt.quizengine.model.Question;
import com.vvt.quizengine.model.Quiz;
import com.vvt.quizengine.model.Response;
import com.vvt.quizengine.model.Solution;
import com.vvt.quizengine.model.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface QuizService {
    public Quiz getQuiz(long id) throws Exception;

    public Quiz update(Quiz quiz);

    public Question update(Question question);

    public Answer update(Answer answer);

    public Solution update(Solution solution);

    public Response update(Response response);

    public void deleteQuiz(Long id);

    public Double caculateScore(Response response) throws Exception;

    public Long getQuestions(Long quizId);

    public List<Solution> getSolutions(Long userId);

    public List<Solution> getSolutionsByQuizId(Long quizId);
}
