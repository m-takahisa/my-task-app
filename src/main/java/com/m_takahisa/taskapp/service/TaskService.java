package com.m_takahisa.taskapp.service;

import com.m_takahisa.taskapp.entity.Task;
import com.m_takahisa.taskapp.entity.User;
import com.m_takahisa.taskapp.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    /**
     * すべてのタスクを取得します
     */
    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * 新しいタスクを保存します
     */
    @Transactional
    public Task createTask(Task task, User user) {
        task.setUser(user); // タスクに所有者（ユーザー）を紐付ける
        return taskRepository.save(task);
    }
}