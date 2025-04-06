package com.nursetrack.controller;

import com.nursetrack.model.User;
import com.nursetrack.repository.UserRepository;
import com.nursetrack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin
    @GetMapping("api/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
