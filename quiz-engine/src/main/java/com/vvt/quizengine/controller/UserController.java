package com.vvt.quizengine.controller;

import com.vvt.quizengine.dto.UserDto;
import com.vvt.quizengine.jwtutils.TokenManager;
import com.vvt.quizengine.jwtutils.Token;
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

    @PostMapping(path = "/login")
    public ResponseEntity<Token> login(@RequestBody UserDto userDto) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword())
            );
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        } catch (Exception e) {
            throw e;
        }
        final UserDetails user = userService.loadUserByUsername(userDto.getEmail());
        final String jwtToken = tokenManager.generateJwtToken(user);
        return new ResponseEntity(new Token(jwtToken), HttpStatus.OK);
    }

    @PostMapping(path = "/register")
    public ResponseEntity<Token> register(@RequestBody UserDto userDto) {
        User user = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();
        userService.update(user);
        final String jwtToken = tokenManager.generateJwtToken(user);
        return new ResponseEntity(new Token(jwtToken), HttpStatus.CREATED);
    }
}