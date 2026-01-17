package com.m_takahisa.taskapp.controller;

import com.m_takahisa.taskapp.entity.Task;
import com.m_takahisa.taskapp.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller // @RestControllerではなく@Controllerを使う
@RequestMapping("/view/tasks")
@RequiredArgsConstructor
public class TaskViewController {

    private final TaskService taskService;

    @GetMapping
    public String listTasks(Model model) {
        // 現在は認証機能が未実装のため、一旦ID:1のユーザーのタスクを取得する想定
        // ※ServiceにfindByUserIdなどが実装されている前提です
        List<Task> tasks = taskService.findAllTasks(); // 全件取得でテスト
        model.addAttribute("tasks", tasks);

        return "tasks/list"; // templates/tasks/list.html を探しに行く
    }
}