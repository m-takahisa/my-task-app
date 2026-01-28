package com.m_takahisa.taskapp.service;

import com.m_takahisa.taskapp.entity.User;
import com.m_takahisa.taskapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 全ユーザーを取得します
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * idでユーザーを検索します
     */
    public Optional<User> findById(Long id) { return userRepository.findByid(id); }

    /**
     * 新しいユーザーを登録します
     */
    @Transactional
    public User createUser(User user) {
        // 重複チェックなどのロジックをここに書くことができます
        return userRepository.save(user);
    }

    /**
     * メールアドレスでユーザーを検索します
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}