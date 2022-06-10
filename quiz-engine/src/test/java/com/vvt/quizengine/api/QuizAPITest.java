package com.vvt.quizengine.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vvt.quizengine.dto.QuizDTO;
import com.vvt.quizengine.dto.UserDTO;
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

    @BeforeAll
    public void init() {
        restTemplate.postForEntity("/users/register",
                new UserDTO("tester1@abc.com", "password"), Object.class);
        restTemplate.postForEntity("/users/register",
                new UserDTO("tester2@abc.com", "password"), Object.class);
    }

    @Test
    public void unauthorizedCreateQuizRequest() {
        ResponseEntity<Object> quizResponse = restTemplate.postForEntity("/quizzes/",
                new QuizDTO("A random quiz."), Object.class);
        assertThat(quizResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @WithUserDetails("tester1@abc.com")
    public void canCreateQuizRequest() throws Exception {
        QuizDTO quizDTO = new QuizDTO("A random quiz.");
        MvcResult quizResponse = this.mockMvc
                .perform(post("/quizzes/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(objectMapper, quizDTO)))
                .andExpect(status().isCreated())
                .andReturn();
    }
}
