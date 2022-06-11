package com.vvt.quizengine.service;

import com.vvt.quizengine.dto.AnswerDTO;
import com.vvt.quizengine.dto.QuestionDTO;
import com.vvt.quizengine.dto.QuizDTO;
import com.vvt.quizengine.model.Answer;
import com.vvt.quizengine.model.Question;
import com.vvt.quizengine.model.Quiz;
import com.vvt.quizengine.model.QuizStatus;
import com.vvt.quizengine.repository.AnswerRepository;
import com.vvt.quizengine.repository.QuestionRepository;
import com.vvt.quizengine.repository.QuizRepository;
import com.vvt.quizengine.utils.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizServiceImpl implements QuizService{
    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private URLEncoder urlEncoder;

    @Override
    public Quiz createQuiz(Long userId, QuizDTO quizDTO) throws Exception {
        Quiz quiz = Quiz.builder()
                .userId(userId)
                .status(QuizStatus.CREATED)
                .title(quizDTO.getTitle())
                .build();
        quiz = update(quiz);
        Long id = quiz.getId();
        String encodedUrl = urlEncoder.encode(id.toString());
        quiz.setEncodedUrl(encodedUrl);
        return update(quiz);
    }

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
    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }

    public Long getWrongAnswers(Long questionId) {
        return answerRepository.getWrongAnswers(questionId);
    }

    @Override
    public Long getCorrectAnswers(Long questionID) {
        return answerRepository.getCorrectAnswers(questionID);
    }

    @Override
    public List<Long> getCorrectAnswerList(Long questionId) {
        return answerRepository.getCorrectAnswerList(questionId);
    }

    @Override
    public List<Long> getWrongAnswerList(Long questionId) {
        return answerRepository.getWrongAnswerList(questionId);
    }

    @Override
    public Long getQuestions(Long quizId) {
        return questionRepository.getQuestions(quizId);
    }

    @Override
    public Question addQuestion(Quiz quiz, QuestionDTO questionDTO) {
        Question question = Question.builder()
                .quizId(questionDTO.getQuizId())
                .text(questionDTO.getText())
                .type(questionDTO.getType())
                .build();
        question = update(question);
        List<Answer> answers = new ArrayList<Answer>();
        for (AnswerDTO answerDto: questionDTO.getAnswers()) {
            Answer answer = Answer.builder()
                    .questionId(question.getId())
                    .value(answerDto.getValue())
                    .correct(answerDto.getCorrect())
                    .build();
            answer = update(answer);
            answers.add(answer);
        }
        question.setAnswers(answers);
        question = update(question);

        quiz.addQuestion(question);
        update(quiz);
        return question;
    }

}
