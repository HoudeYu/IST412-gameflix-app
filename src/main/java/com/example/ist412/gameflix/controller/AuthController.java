package com.example.ist412.gameflix.controller;

import com.example.ist412.gameflix.dto.UserDTO;
import com.example.ist412.gameflix.model.User;
import com.example.ist412.gameflix.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 注册接口
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findByUsername(userDTO.getUsername());

        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Username already exists"));
        }

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        User user = new User(userDTO.getUsername(), encodedPassword);
        userRepository.save(user);

        return ResponseEntity.ok(Collections.singletonMap("message", "User registered successfully"));
    }

    // 登录接口
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody UserDTO userDTO) {
        Optional<User> userOpt = userRepository.findByUsername(userDTO.getUsername());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "Invalid username or password"));
        }

        User user = userOpt.get();
        if (passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            return ResponseEntity.ok(Collections.singletonMap("message", "Login successful"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "Invalid username or password"));
        }
    }
}
