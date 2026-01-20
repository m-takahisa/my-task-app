package com.m_takahisa.taskapp.controller;

import com.m_takahisa.taskapp.entity.Task;
import com.m_takahisa.taskapp.entity.User;
import com.m_takahisa.taskapp.service.TaskService;
import com.m_takahisa.taskapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller // @RestControllerではなく@Controllerを使う
@RequestMapping("/view/tasks")
@RequiredArgsConstructor
public class TaskViewController {

    private final TaskService taskService;
    private final UserService userService;

    @GetMapping
    public String listTasks(Model model) {
        // 現在は認証機能が未実装のため、一旦ID:1のユーザーのタスクを取得する想定
        // ※ServiceにfindByUserIdなどが実装されている前提です
        List<Task> tasks = taskService.findAllTasks(); // 全件取得でテスト
        model.addAttribute("tasks", tasks);

        return "tasks/list"; // templates/tasks/list.html を探しに行く
    }

    /**
     * 登録画面を表示する
     */
    @PostMapping("/create")
    public String createTask(@ModelAttribute Task task) {
        // 本来はログインユーザーをセットしますが、一旦特定のユーザー(ID:1)に紐付けます
        User user = userService.findById(1L)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
         task.setUser(user);

        taskService.save(task); // Service経由でDB保存
        return "redirect:/view/tasks"; // 保存後は一覧画面へリダイレクト
    }

    /**
     * 保存処理を行う
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("task", new Task()); // 空のTaskオブジェクトをフォームに渡す
        return "tasks/create"; // templates/tasks/create.html を表示
    }
}