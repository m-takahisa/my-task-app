package com.m_takahisa.taskapp.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    /**
     * ユーザーを取得
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザー名が見つかりません: " + username));

        // UserDetailsImplでラップして返す
        return new UserDetailsImpl(user);
    }

    /**
     * ユーザーを登録
     */
    @Transactional
    public void registerUser(UserRegistrationRequest request, Locale locale) {
        // メールアドレスの重複チェック
        String getMessage = messageSource.getMessage("user.register.generic_error", null, locale);
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new UserException.AlreadyExistsException(getMessage);
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        // パスワードを暗号化
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
    }
}