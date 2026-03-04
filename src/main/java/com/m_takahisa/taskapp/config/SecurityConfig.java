package com.m_takahisa.taskapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**").permitAll() // 静的ファイルは全員許可
                        .anyRequest().authenticated() // それ以外はすべてログインが必要
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/view/tasks", true) // ログイン後の遷移先
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        // BCryptPasswordEncoder を使って "password" をハッシュ化(動作確認用)
        System.out.println(new BCryptPasswordEncoder().encode("password"));

        return http.build();
    }
    // SecurityConfig.java 内に追加
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // これで「BCrypt形式で照合する」と設定される
    }
}