package com.m_takahisa.taskapp.controller;

import com.m_takahisa.taskapp.entity.Task;
import com.m_takahisa.taskapp.entity.User;
import com.m_takahisa.taskapp.service.TaskService;
import com.m_takahisa.taskapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    /**
     * タスク一覧をJSONで返します
     * GET http://localhost:8085/tasks
     */
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.findAllTasks();
    }

    /**
     * テスト用のタスクを作成し、既存のユーザーに紐付けます
     * GET http://localhost:8085/tasks/test
     */
    @GetMapping("/test")
    public String createTestTask() {
        // 先ほど作成した admin@m_takahisa.com のユーザーを取得
        User user = userService.findByEmail("admin@m_takahisa.com")
                .orElseThrow(() -> new RuntimeException("User not found. Please run /users/test first."));

        Task task = new Task();
        task.setTitle("Docker環境の構築");
        task.setDescription("PostgreSQLとpgAdminを立ち上げる");
        task.setCompleted(true);

        taskService.createTask(task, user);

        return "Task has been created and assigned to m_takahisa!";
    }
}