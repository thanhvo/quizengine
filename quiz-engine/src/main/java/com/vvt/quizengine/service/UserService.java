package com.vvt.quizengine.service;

import com.vvt.quizengine.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {

    public Iterable<User> getAllUsers();

    public User getUser(long id) throws UsernameNotFoundException;

    public User update(User user);

    public void delete(User user);
}