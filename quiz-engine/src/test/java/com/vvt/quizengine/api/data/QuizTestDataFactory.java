package com.vvt.quizengine.api.data;

import com.vvt.quizengine.dto.QuizDTO;
import com.vvt.quizengine.exception.ApiError;
import com.vvt.quizengine.model.Quiz;
import com.vvt.quizengine.model.QuizStatus;
import com.vvt.quizengine.service.QuizService;
import com.vvt.quizengine.utils.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Component
public class QuizTestDataFactory {
    @Autowired
    private QuizService quizService;

    @Autowired
    private URLEncoder urlEncoder;

    public Quiz createQuiz(Long userId, QuizDTO quizDTO) throws Exception{
        Quiz quiz = Quiz.builder()
                .userId(userId)
                .status(QuizStatus.CREATED)
                .title(quizDTO.getTitle())
                .build();
        quiz = this.quizService.update(quiz);
        Long id = quiz.getId();
        String encodedUrl = urlEncoder.encode(id.toString());
        quiz.setEncodedUrl(encodedUrl);
        return this.quizService.update(quiz);
    }
}
