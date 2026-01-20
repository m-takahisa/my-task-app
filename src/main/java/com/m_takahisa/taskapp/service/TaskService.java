package com.m_takahisa.taskapp.service;

import com.m_takahisa.taskapp.entity.Task;
import com.m_takahisa.taskapp.entity.User;
import com.m_takahisa.taskapp.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
     * Taskのみを受け取って保存するメソッド
     * フォームから送信されたTaskオブジェクトをそのまま保存する場合に使用
     */
    @Transactional
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    /**
     * ユーザーを紐付けて保存するメソッド
     */
    @Transactional
    public Task createTask(Task task, User user) {
        task.setUser(user); // タスクに所有者（ユーザー）を紐付ける
        return taskRepository.save(task);
    }

    /**
     * 指定したIDのタスクを削除します
     */
    @Transactional
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    /**
     * 指定したIDのタスクを取得します
     */
    public Optional<Task> findTaskById(Long id) {
        return taskRepository.findById(id);
    }
}