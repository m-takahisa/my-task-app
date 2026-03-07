package com.m_takahisa.taskapp.auth;

import com.m_takahisa.taskapp.entity.User;
import com.m_takahisa.taskapp.repository.UserRepository;
import com.m_takahisa.taskapp.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // DBからユーザー名で検索。UserRepositoryにfindByUsernameの定義が必要です。
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザー名が見つかりません: " + username));

        // UserDetailsImplでラップして返す
        return new UserDetailsImpl(user);
    }
}