package com.vvt.quizengine.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vvt.quizengine.api.data.QuizTestDataFactory;
import com.vvt.quizengine.api.data.UserTestDataFactory;
import com.vvt.quizengine.dto.AnswerDTO;
import com.vvt.quizengine.dto.QuestionDTO;
import com.vvt.quizengine.dto.QuizDTO;
import com.vvt.quizengine.dto.UserDTO;
import com.vvt.quizengine.model.QuestionType;
import com.vvt.quizengine.model.Quiz;
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

import static com.vvt.quizengine.utils.JsonHelper.toJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@WithMockUser
public class QuizAPITest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private QuizTestDataFactory quizTestDataFactory;

    @Autowired
    private UserTestDataFactory userTestDataFactory;

    private static final String USER1_NAME = "tester1@abc.com";

    private static final String USER2_NAME = "tester2@abc.com";

    @BeforeAll
    public void init() {
        restTemplate.postForEntity("/users/register",
                new UserDTO(USER1_NAME, "password"), Object.class);
        restTemplate.postForEntity("/users/register",
                new UserDTO(USER2_NAME, "password"), Object.class);
    }

    @Test
    public void unauthorizedCreateQuizRequest() {
        ResponseEntity<Object> quizResponse = restTemplate.postForEntity("/quizzes/",
                new QuizDTO("A random quiz."), Object.class);
        assertThat(quizResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @WithUserDetails(USER1_NAME)
    public void canCreateQuizRequest() throws Exception {
        QuizDTO quizDTO = new QuizDTO("A random quiz.");
        this.mockMvc
                .perform(post("/quizzes/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(objectMapper, quizDTO)))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    @WithUserDetails(USER1_NAME)
    public void addSingleQuestion() throws Exception {
        QuizDTO quizDTO = new QuizDTO("A simple quiz");
        User user = userTestDataFactory.getUser(USER1_NAME);
        Quiz quiz = quizTestDataFactory.createQuiz(user.getId(), quizDTO);
        QuestionDTO question = QuestionDTO.builder()
                .quizId(quiz.getId())
                .text("Moon is a star?")
                .type(QuestionType.SINGLE)
                .build();
        question.addAnswer(new AnswerDTO("Yes", true));
        question.addAnswer(new AnswerDTO("No", false));
        this.mockMvc
                .perform(post("/quizzes/addQuestion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(objectMapper, question)))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    @WithUserDetails(USER1_NAME)
    public void addMultipleQuestion() throws Exception {
        QuizDTO quizDTO = new QuizDTO("A simple quiz");
        User user = userTestDataFactory.getUser(USER1_NAME);
        Quiz quiz = quizTestDataFactory.createQuiz(user.getId(), quizDTO);
        QuestionDTO question = QuestionDTO.builder()
                .quizId(quiz.getId())
                .text("Temperature can be measured in?")
                .type(QuestionType.MULTIPLE)
                .build();
        question.addAnswer(new AnswerDTO("Kelvin", true));
        question.addAnswer(new AnswerDTO("Fahrenheit", true));
        question.addAnswer(new AnswerDTO("Gram", false));
        question.addAnswer(new AnswerDTO("Celsius", true));
        question.addAnswer(new AnswerDTO("Liters", false));
        this.mockMvc
                .perform(post("/quizzes/addQuestion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(objectMapper, question)))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    @WithUserDetails(USER1_NAME)
    public void canPublishQuiz() throws Exception {
        QuizDTO quizDTO = new QuizDTO("A simple quiz");
        User user = userTestDataFactory.getUser(USER1_NAME);
        Quiz quiz = quizTestDataFactory.createQuiz(user.getId(), quizDTO);
        this.mockMvc
                .perform(post("/quizzes/publish/" + quiz.getEncodedUrl()))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithUserDetails(USER1_NAME)
    public void cannotPublishQuizWithID() throws Exception {
        QuizDTO quizDTO = new QuizDTO("A simple quiz");
        User user = userTestDataFactory.getUser(USER1_NAME);
        Quiz quiz = quizTestDataFactory.createQuiz(user.getId(), quizDTO);
        this.mockMvc
                .perform(post("/quizzes/publish/" + quiz.getId()))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @WithUserDetails(USER1_NAME)
    public void cannotPublishOthersQuiz() throws Exception {
        QuizDTO quizDTO = new QuizDTO("A simple quiz");
        User user = userTestDataFactory.getUser(USER2_NAME);
        Quiz quiz = quizTestDataFactory.createQuiz(user.getId(), quizDTO);
        this.mockMvc
                .perform(post("/quizzes/publish/" + quiz.getEncodedUrl()))
                .andExpect(status().isForbidden())
                .andReturn();
    }
}
