package com.m_takahisa.taskapp.controller;

import com.m_takahisa.taskapp.entity.Notification;
import com.m_takahisa.taskapp.entity.Task;
import com.m_takahisa.taskapp.entity.TaskStatus;
import com.m_takahisa.taskapp.entity.User;
import com.m_takahisa.taskapp.service.TaskService;
import com.m_takahisa.taskapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.m_takahisa.taskapp.repository.NotificationRepository;

import java.util.List;

@Controller // @RestControllerではなく@Controllerを使う
@RequestMapping("/view/tasks")
@RequiredArgsConstructor
public class TaskViewController {

    private final TaskService taskService;
    private final UserService userService;
    private final NotificationRepository notificationRepository;

    /**
     * 検索を行う
     */
    @GetMapping
    public String listTasks(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "status", required = false) TaskStatus status,
            Model model) {

        // タスク一覧の取得
        List<Task> tasks = taskService.searchTasks(keyword, status);

        // 未読通知の取得(本来はログインユーザーを渡しますが、現在は全未読通知を取得する)
        List<Notification> unreadNotifications = notificationRepository.findByIsReadFalseOrderByCreatedAtDesc();

        // Modelへの追加
        model.addAttribute("tasks", tasks);
        model.addAttribute("unreadNotifications", unreadNotifications);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedStatus", status); // 選択状態の保持用
        model.addAttribute("statusOptions", TaskStatus.values()); // セレクトボックスの選択肢

        return "tasks/list";
    }

    /**
     * 登録画面を表示する
     */
    @PostMapping("/create")
    public String createTask(@Validated @ModelAttribute Task task, BindingResult bindingResult) {
        // 入力エラーがある場合は、登録画面に戻す
        if (bindingResult.hasErrors()) {
            return "tasks/create";
        }

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

    /**
     * 削除処理を行う
     */
    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/view/tasks"; // 削除後は一覧へ戻る
    }

    /**
     * 編集画面を表示する
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Task task = taskService.findTaskById(id)
                .orElseThrow(() -> new RuntimeException("タスクが見つかりません ID: " + id));
        model.addAttribute("task", task);
        return "tasks/edit"; // templates/tasks/edit.html を表示
    }

    /**
     * タスクを更新する
     */
    @PostMapping("/{id}/update")
    public String updateTask(@PathVariable Long id, @Validated @ModelAttribute Task task, BindingResult bindingResult) {
        // 入力エラーがある場合は、編集画面に戻す
        if (bindingResult.hasErrors()) {
            return "tasks/edit";
        }

        // 既存のデータを取得して更新する（安全な実装方法）
        Task existingTask = taskService.findTaskById(id)
                .orElseThrow(() -> new RuntimeException("タスクが見つかりません ID: " + id));

        // フォームから送られてきた内容で上書き
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setStatus(task.getStatus());
        existingTask.setCompleted(task.isCompleted());

        taskService.save(existingTask);
        return "redirect:/view/tasks";
    }

    // 通知の既読処理
    @PostMapping("/notifications/{id}/read")
    public String markAsRead(@PathVariable Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("通知が見つかりません ID: " + id));

        // 既読に設定して保存
        notification.setRead(true);
        notificationRepository.save(notification);

        // 一覧画面にリダイレクト（これで通知が消える）
        return "redirect:/view/tasks";
    }
}