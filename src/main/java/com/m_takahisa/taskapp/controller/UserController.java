package com.m_takahisa.taskapp.controller;

import com.m_takahisa.taskapp.entity.User;
import com.m_takahisa.taskapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 全ユーザーの一覧を返します
     * GET http://localhost:8085/users
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    /**
     * テスト用のユーザーを1人作成します
     * GET http://localhost:8085/users/test
     */
    @GetMapping("/test")
    public String createTestUser() {
        User user = new User();
        user.setName("m_takahisa");
        user.setEmail("admin@m_takahisa.com");
        user.setPassword("password123");

        userService.createUser(user);

        return "User 'm_takahisa' has been created successfully!";
    }
}