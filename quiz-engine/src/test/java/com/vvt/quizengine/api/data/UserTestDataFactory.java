package com.vvt.quizengine.api.data;

import com.vvt.quizengine.model.User;
import com.vvt.quizengine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserTestDataFactory {
    @Autowired
    private UserService userService;

    public User getUser(String email) {
        return (User) userService.loadUserByUsername(email);
    }
}
