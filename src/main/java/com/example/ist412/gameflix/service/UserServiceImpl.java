package com.example.ist412.gameflix.service;

import com.example.ist412.gameflix.model.User;
import com.example.ist412.gameflix.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(); // 密码加密器
    }

    @Override
    public String registerUser(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            return "Username already exists";
        }
        String hashedPassword = passwordEncoder.encode(password);
        User newUser = new User(username, hashedPassword);
        userRepository.save(newUser);
        return "User registered successfully";
    }

    @Override
    public String loginUser(String username, String password) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return "Login successful";
        } else {
            return "Invalid username or password";
        }
    }
}
