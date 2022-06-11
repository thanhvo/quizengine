package com.vvt.quizengine.api;

import com.vvt.quizengine.api.data.UserTestDataFactory;
import com.vvt.quizengine.dto.UserDTO;
import com.vvt.quizengine.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserAPITest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserTestDataFactory userTestDataFactory;

    private static final String USER_NAME = "tester@abc.com";

    private static final String PASSWORD = "password";

    @Test
    public void canCreateUser() {
        try {
            User user = userTestDataFactory.getUser(USER_NAME);
            userTestDataFactory.deleteUser(user);
        } catch( Exception ex) {}

        ResponseEntity<Object> userResponse = restTemplate.postForEntity("/users/register",
                new UserDTO(USER_NAME, PASSWORD), Object.class);
        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void cannotCreateDuplicateUser() {
        try {
            User user = userTestDataFactory.getUser(USER_NAME);
        } catch( Exception ex) {
            userTestDataFactory.createUser(USER_NAME, PASSWORD);
        }
        ResponseEntity<Object> userResponse = restTemplate.postForEntity("/users/register",
                new UserDTO(USER_NAME, PASSWORD), Object.class);
        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void cannotCreateUserWithInvalidEmail() {
        ResponseEntity<Object> userResponse = restTemplate.postForEntity("/users/register",
                new UserDTO("abbc123", "password"), Object.class);
        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void canLogin() {
        try {
            User user = userTestDataFactory.getUser(USER_NAME);
        } catch( Exception ex) {
            userTestDataFactory.createUser(USER_NAME, PASSWORD);
        }
        ResponseEntity<Object> loginResponse = restTemplate.postForEntity("/users/login",
                new UserDTO(USER_NAME, PASSWORD), Object.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void cannotLogin() {
        ResponseEntity<Object> loginResponse = restTemplate.postForEntity("/users/login",
                new UserDTO("thanhvv@gmail.com", "wrongpassword"), Object.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
