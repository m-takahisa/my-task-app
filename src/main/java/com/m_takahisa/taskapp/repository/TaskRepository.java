package com.m_takahisa.taskapp.repository;

import com.m_takahisa.taskapp.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // ユーザーIDに紐づくタスクだけを抽出するメソッド（後で使います）
    List<Task> findByUserId(Long userId);
}