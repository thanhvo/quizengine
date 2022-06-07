package com.vvt.quizengine.service;

import com.vvt.quizengine.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {

    Iterable<User> getAllUsers();

    User getUser(long id) throws UsernameNotFoundException;

    User update(User user);
}