package com.tradingjournal.trading_journal.service;


import com.tradingjournal.trading_journal.dtos.SignupRequest;
import com.tradingjournal.trading_journal.models.User;
import com.tradingjournal.trading_journal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public User registerUser(SignupRequest signup) {
        User user = new User();
        user.setEmail(signup.getEmail());
        user.setUsername(signup.getUsername());
        user.setPassword(passwordEncoder.encode(signup.getPassword())); // encrypt
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}

