package com.vvt.quizengine.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vvt.quizengine.api.data.QuizTestDataFactory;
import com.vvt.quizengine.api.data.UserTestDataFactory;
import com.vvt.quizengine.dto.AnswerDTO;
import com.vvt.quizengine.dto.QuestionDTO;
import com.vvt.quizengine.dto.QuizDTO;
import com.vvt.quizengine.dto.ResponseDTO;
import com.vvt.quizengine.dto.ScoreDTO;
import com.vvt.quizengine.dto.SolutionDTO;
import com.vvt.quizengine.model.Question;
import com.vvt.quizengine.model.QuestionType;
import com.vvt.quizengine.model.Quiz;
import com.vvt.quizengine.model.QuizStatus;
import com.vvt.quizengine.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static com.vvt.quizengine.utils.JsonHelper.fromJson;
import static com.vvt.quizengine.utils.JsonHelper.toJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@WithMockUser
public class SolutionAPITest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private QuizTestDataFactory quizTestDataFactory;

    @Autowired
    private UserTestDataFactory userTestDataFactory;

    private static final String USER1_NAME = "tester1@abc.com";

    private static final String USER2_NAME = "tester2@abc.com";

    private static final String PASSWORD = "password";

    private Quiz quiz;
    private Question question1, question2;
    private User user1, user2;

    @BeforeAll
    public void init() throws Exception {
        try {
            User user = userTestDataFactory.getUser(USER1_NAME);
        } catch( Exception ex) {
            userTestDataFactory.createUser(USER1_NAME, PASSWORD);
        }
        try {
            User user = userTestDataFactory.getUser(USER2_NAME);
        } catch( Exception ex) {
            userTestDataFactory.createUser(USER2_NAME, PASSWORD);
        }
        QuizDTO quizDTO = new QuizDTO("A simple quiz");
        user1 = userTestDataFactory.getUser(USER1_NAME);
        this.quiz = quizTestDataFactory.createQuiz(user1.getId(), quizDTO);
        QuestionDTO questionDto = QuestionDTO.builder()
                .quizId(quiz.getId())
                .text("Moon is a star?")
                .type(QuestionType.SINGLE)
                .build();
        questionDto.addAnswer(new AnswerDTO("Yes", true));
        questionDto.addAnswer(new AnswerDTO("No", false));
        this.question1 = this.quizTestDataFactory.addQuestion(quiz, questionDto);
        questionDto = QuestionDTO.builder()
                .quizId(quiz.getId())
                .text("Temperature can be measured in?")
                .type(QuestionType.MULTIPLE)
                .build();
        questionDto.addAnswer(new AnswerDTO("Kelvin", true));
        questionDto.addAnswer(new AnswerDTO("Fahrenheit", true));
        questionDto.addAnswer(new AnswerDTO("Gram", false));
        questionDto.addAnswer(new AnswerDTO("Celsius", true));
        questionDto.addAnswer(new AnswerDTO("Liters", false));
        this.question2 = this.quizTestDataFactory.addQuestion(quiz, questionDto);
    }

    @Test
    @WithUserDetails(USER2_NAME)
    public void canCreateSolution1() throws Exception {
        quizTestDataFactory.publishQuiz(quiz);
        SolutionDTO solution = new SolutionDTO(quiz.getId());
        List<Long> answerIds = quizTestDataFactory.getCorrectAnswers(question1.getId());
        ResponseDTO response = new ResponseDTO(question1.getId(), answerIds);
        solution.addResponse(response);
        answerIds = quizTestDataFactory.getCorrectAnswers(question2.getId());
        response = new ResponseDTO(question2.getId(), answerIds);
        solution.addResponse(response);
        MvcResult solutionResult = this.mockMvc
                .perform(post("/solutions/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(objectMapper, solution)))
                .andExpect(status().isCreated())
                .andReturn();
        ScoreDTO scoreDto = fromJson(objectMapper, solutionResult.getResponse().getContentAsString(), ScoreDTO.class);
        assertEquals(scoreDto.getTotalScore(), "100%");
    }

    @Test
    @WithUserDetails(USER2_NAME)
    public void canCreateSolution2() throws Exception {
        quizTestDataFactory.publishQuiz(quiz);
        SolutionDTO solution = new SolutionDTO(quiz.getId());
        List<Long> answerIds = quizTestDataFactory.getCorrectAnswers(question1.getId());
        ResponseDTO response = new ResponseDTO(question1.getId(), answerIds);
        solution.addResponse(response);
        List<Long> correctAnswers = quizTestDataFactory.getCorrectAnswers(question2.getId());
        List<Long> wrongAnswers = quizTestDataFactory.getWrongAnswers(question2.getId());
        answerIds = Arrays.asList(wrongAnswers.get(0),correctAnswers.get(0),correctAnswers.get(1));
        response = new ResponseDTO(question2.getId(), answerIds);
        solution.addResponse(response);
        MvcResult solutionResult = this.mockMvc
                .perform(post("/solutions/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(objectMapper, solution)))
                .andExpect(status().isCreated())
                .andReturn();
        ScoreDTO scoreDto = fromJson(objectMapper, solutionResult.getResponse().getContentAsString(), ScoreDTO.class);
        assertEquals(scoreDto.getTotalScore(), "58%");
    }

    @Test
    @WithUserDetails(USER2_NAME)
    public void cannotTakeUnpublishedQuiz() throws Exception {
        quiz.setStatus(QuizStatus.MODIFIED);
        quizTestDataFactory.update(quiz);
        SolutionDTO solution = new SolutionDTO(quiz.getId());
        this.mockMvc
                .perform(post("/solutions/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(objectMapper, solution)))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @WithUserDetails(USER2_NAME)
    public void cannotChooseInvalidAnswer() throws Exception {
        quizTestDataFactory.publishQuiz(quiz);
        SolutionDTO solution = new SolutionDTO(quiz.getId());
        List<Long> answerIds = Arrays.asList(0L);
        ResponseDTO response = new ResponseDTO(question1.getId(), answerIds);
        solution.addResponse(response);
        this.mockMvc
                .perform(post("/solutions/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(objectMapper, solution)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @WithUserDetails(USER1_NAME)
    public void canGetSolutions() throws Exception {
        this.mockMvc
                .perform(get("/solutions/"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void unauthorizedGetSolutionRequest() {
        ResponseEntity<Object> solutionResponse = testRestTemplate.getForEntity("/solutions/", Object.class);
        assertThat(solutionResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @WithUserDetails(USER1_NAME)
    public void canGetSolutionsByQuiz() throws Exception {
        this.mockMvc
                .perform(get("/solutions/byQuiz/" + quiz.getEncodedUrl()))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithUserDetails(USER2_NAME)
    public void cannotGetSolutionsByQuizOfOthers() throws Exception {
        this.mockMvc
                .perform(get("/solutions/byQuiz/" + quiz.getEncodedUrl()))
                .andExpect(status().isForbidden())
                .andReturn();
    }
}
