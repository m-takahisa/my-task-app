package com.m_takahisa.taskapp.task;

import com.m_takahisa.taskapp.auth.User;
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
     * ユーザー専用のタスク一覧取得
     */
    public List<Task> findTasksByUser(User user) {
        return taskRepository.findByUserOrderByDueDateAsc(user);
    }

    /**
     * タスクの登録処理
     */
    @Transactional
    public Task save(TaskRequest request, User user) {
        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setDueDate(request.dueDate());
        task.setStatus(request.status());
        task.setCompleted(request.completed());
        task.setUser(user);
        return taskRepository.save(task);
    }

    /**
     * タスクの更新処理
     */
    @Transactional
    public Task update(Long id, TaskRequest request) {
        // 既存データを取得（なければエラー）
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("タスクが見つかりません ID: " + id));

        // DTOの内容でEntityを上書き
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setDueDate(request.dueDate());
        task.setStatus(request.status());
        task.setCompleted(request.completed());
        return taskRepository.save(task);
    }

    /**
     * タスクの削除処理
     */
    @Transactional
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    /**
     * タスクの取得処理
     */
    public Optional<Task> findTaskById(Long id) {
        return taskRepository.findById(id);
    }

}