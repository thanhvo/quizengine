package com.vvt.quizengine.service;

import com.vvt.quizengine.model.Answer;
import com.vvt.quizengine.model.Question;
import com.vvt.quizengine.model.Quiz;
import com.vvt.quizengine.model.Response;
import com.vvt.quizengine.model.Solution;
import com.vvt.quizengine.repository.AnswerRepository;
import com.vvt.quizengine.repository.QuestionRepository;
import com.vvt.quizengine.repository.QuizRepository;
import com.vvt.quizengine.repository.ResponseRepository;
import com.vvt.quizengine.repository.SolutionRepository;
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

    @Autowired
    private SolutionRepository solutionRepository;

    @Autowired
    private ResponseRepository responseRepository;

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

    @Override
    public Solution update(Solution solution) {
        return solutionRepository.save(solution);
    }

    @Override
    public Response update(Response response) {
        return responseRepository.save(response);
    }

    @Override
    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }

    private Long getWrongAnswers(Long questionId) {
        return answerRepository.getWrongAnswers(questionId);
    }

    private Long getCorrectAnswers(Long questionID) {
        return answerRepository.getCorrectAnswers(questionID);
    }

    @Override
    public Double caculateScore(Response response) throws Exception {
        int correctReponses = 0, wrongResponses = 0;
        int wrongAnswers = getWrongAnswers(response.getQuestionId()).intValue();
        int correctAnswers = (int)getCorrectAnswers(response.getQuestionId()).intValue();
        for (Long answerId: response.getAnswerIds() ) {
            Answer answer = this.answerRepository.findById(answerId)
                    .orElseThrow(() -> new Exception("Answer not found!"));
            if (answer.getCorrect()) {
                correctReponses++;
            } else {
                wrongResponses++;
            }
        }
        return (double) correctReponses / correctAnswers - (double) wrongResponses / wrongAnswers;
    }

    @Override
    public Long getQuestions(Long quizId) {
        return questionRepository.getQuestions(quizId);
    }

}
