package com.vvt.quizengine.api.data;

import com.vvt.quizengine.model.User;
import com.vvt.quizengine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserTestDataFactory {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUser(String email) {
        return (User) userService.loadUserByUsername(email);
    }

    public User createUser(String email, String password) {
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        return userService.update(user);
    }

    public void deleteUser(User user) {
        userService.delete(user);
    }
}
