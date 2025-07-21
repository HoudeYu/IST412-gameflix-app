package com.example.ist412.gameflix.service;

import com.example.ist412.gameflix.model.User;

public interface UserService {
    String registerUser(String username, String password);
    String loginUser(String username, String password);
}
