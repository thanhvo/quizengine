package com.vvt.quizengine.service;

import com.vvt.quizengine.model.User;
import com.vvt.quizengine.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;


@Service
@Transactional
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(long id) throws UsernameNotFoundException {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("user"));
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException{
        User user = userRepository.findByEmail(email);
        if (user == null) throw new UsernameNotFoundException(email);
        return user;
    }
}
