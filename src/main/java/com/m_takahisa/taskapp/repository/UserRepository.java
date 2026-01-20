package com.m_takahisa.taskapp.repository;

import com.m_takahisa.taskapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // idでユーザーを探すメソッド（名前からSQLが自動生成されます）
    Optional<User> findByid(Long id);

    // メールアドレスでユーザーを探すメソッド（名前からSQLが自動生成されます）
    Optional<User> findByEmail(String email);
}