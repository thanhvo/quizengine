package com.vvt.quizengine.controller;

import com.vvt.quizengine.dto.UserDTO;
import com.vvt.quizengine.exception.ApiError;
import com.vvt.quizengine.utils.TokenManager;
import com.vvt.quizengine.utils.Token;
import com.vvt.quizengine.model.User;
import com.vvt.quizengine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenManager tokenManager;

    @GetMapping(value = {"", "/"})
    public Iterable<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(value = {"/{id}"})
    public User getUser(@PathVariable Long id) throws UsernameNotFoundException {
        return userService.getUser(id);
    }

    private boolean isValidEmail(String email) {
        String regexPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return Pattern.compile(regexPattern).matcher(email).matches();
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Object> login(@RequestBody UserDTO userDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword())
            );
        } catch (DisabledException e) {
            ApiError error = new ApiError(HttpStatus.BAD_REQUEST, "The user is disabled.");
            return new ResponseEntity<Object>(error, error.getStatus());
        } catch (BadCredentialsException e) {
            ApiError error = new ApiError(HttpStatus.BAD_REQUEST, "Bad Credential.");
            return new ResponseEntity<Object>(error, error.getStatus());
        } catch (Exception e) {
            ApiError error = new ApiError(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<Object>(error, error.getStatus());
        }
        final UserDetails user = userService.loadUserByUsername(userDto.getEmail());
        final String jwtToken = tokenManager.generateJwtToken(user);
        return new ResponseEntity(new Token(jwtToken), HttpStatus.OK);
    }

    @PostMapping(path = "/register")
    public ResponseEntity<Object> register(@RequestBody UserDTO userDto) {
        User user = null;
        try {
            String email = userDto.getEmail();
            if (!isValidEmail(email)) {
                ApiError error = new ApiError(HttpStatus.BAD_REQUEST, "The email is invalid.");
                return new ResponseEntity<Object>(error, error.getStatus());
            }
            user = User.builder()
                    .email(email)
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .build();
            userService.update(user);
        } catch (Exception ex) {
            ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
            return new ResponseEntity<Object>(error, error.getStatus());
        }
        final String jwtToken = tokenManager.generateJwtToken(user);
        return new ResponseEntity(new Token(jwtToken), HttpStatus.CREATED);
    }
}