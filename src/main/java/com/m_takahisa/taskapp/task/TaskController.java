package com.m_takahisa.taskapp.task;

import com.m_takahisa.taskapp.auth.User;
import com.m_takahisa.taskapp.auth.UserDetailsImpl;
import com.m_takahisa.taskapp.task.notification.Notification;
import com.m_takahisa.taskapp.task.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller // @RestControllerではなく@Controllerを使う
@RequestMapping("/view/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final NotificationRepository notificationRepository;
    private final TaskRepository taskRepository;

    /**
     * 検索を行う
     */
    @GetMapping
    public String listTasks(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Model model) {

        // ログインユーザーを取得
        User currentUser = userDetails.getUser();

        // このユーザーのデータだけを各Repositoryから取得するように変更
        model.addAttribute("tasks", taskRepository.findByUserOrderByDueDateAsc(currentUser));
        model.addAttribute("notifications", notificationRepository.findByUserAndIsReadFalse(currentUser));

        return "tasks/list";
    }

    /**
     * 登録画面を表示する
     */
    @PostMapping("/create")
    public String createTask(@Validated @ModelAttribute Task task, BindingResult bindingResult,
                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 入力エラーがある場合は、登録画面に戻す
        if (bindingResult.hasErrors()) {
            return "tasks/create";
        }

        // ログイン中のユーザーをセット
        task.setUser(userDetails.getUser());

        taskService.save(task, userDetails.getUser());
        return "redirect:/view/tasks";
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
    public String updateTask(@PathVariable Long id, @Validated @ModelAttribute Task task, BindingResult bindingResult,
                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
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

        taskService.save(existingTask, userDetails.getUser());
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