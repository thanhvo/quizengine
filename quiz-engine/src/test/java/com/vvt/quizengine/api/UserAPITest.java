package com.vvt.quizengine.api;

import com.vvt.quizengine.dto.UserDTO;
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

    @Test
    public void canCreateUser() {
        ResponseEntity<Object> userResponse = restTemplate.postForEntity("/users/register",
                new UserDTO("abc@xyz.com", "password"), Object.class);
        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void cannotCreateDuplicateUser() {
        ResponseEntity<Object> userResponse = restTemplate.postForEntity("/users/register",
                new UserDTO("thanhvv@gmail.com", "password"), Object.class);
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
        ResponseEntity<Object> userResponse = restTemplate.postForEntity("/users/register",
                new UserDTO("abc123@xyz.com", "password"), Object.class);
        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ResponseEntity<Object> loginResponse = restTemplate.postForEntity("/users/login",
                new UserDTO("abc123@xyz.com", "password"), Object.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void cannotLogin() {
        ResponseEntity<Object> loginResponse = restTemplate.postForEntity("/users/login",
                new UserDTO("thanhvv@gmail.com", "wrongpassword"), Object.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
