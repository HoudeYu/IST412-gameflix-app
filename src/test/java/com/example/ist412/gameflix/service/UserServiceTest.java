package com.example.ist412.gameflix.service;

import com.example.ist412.gameflix.model.User;
import com.example.ist412.gameflix.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        // 每次测试前清空数据库，避免用户名冲突
        userRepository.deleteAll();
    }

    @Test
    public void testRegisterUserSuccessfully() {
        String result = userService.registerUser("testuser", "testpass");
        Assertions.assertEquals("User registered successfully", result);
        Assertions.assertTrue(userRepository.existsByUsername("testuser"));
    }

    @Test
    public void testRegisterDuplicateUserFails() {
        userService.registerUser("duplicate", "pass1");
        String result = userService.registerUser("duplicate", "pass2");
        Assertions.assertEquals("Username already exists", result);
    }

    @Test
    public void testLoginSuccessAndFailure() {
        userService.registerUser("loginuser", "mypassword");

        String successLogin = userService.loginUser("loginuser", "mypassword");
        Assertions.assertEquals("Login successful", successLogin);

        String failedLogin = userService.loginUser("loginuser", "wrongpassword");
        Assertions.assertEquals("Invalid username or password", failedLogin);
    }
}
