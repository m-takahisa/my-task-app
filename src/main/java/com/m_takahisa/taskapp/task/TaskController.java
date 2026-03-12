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
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "status", required = false) TaskStatus status,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Model model) {

        // ログインユーザーを取得
        User user = userDetails.getUser();

        model.addAttribute("tasks", taskService.searchTasks(keyword, status));
//        model.addAttribute("tasks", taskRepository.findByUserOrderByDueDateAsc(currentUser));
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedStatus", status); // 選択状態を保持するために渡す
        model.addAttribute("notifications", notificationRepository.findByUserAndIsReadFalse(user));

        return "tasks/list";
    }

    /**
     * 登録画面を表示する
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("task", new TaskRequest("", "", java.time.LocalDate.now(), TaskStatus.TODO, false));
        return "tasks/create";
    }

    /**
     * 保存処理を行う
     */
    @PostMapping("/create")
    public String createTask(@Validated @ModelAttribute("task") TaskRequest taskRequest,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 入力エラーがある場合は、登録画面に戻す
        if (bindingResult.hasErrors()) {
            return "tasks/create";
        }

        taskService.save(taskRequest);
        return "redirect:/view/tasks";
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

        TaskRequest taskRequest = new TaskRequest(
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getStatus(),
                task.isCompleted()
        );

        model.addAttribute("task", taskRequest);
        model.addAttribute("taskId", id);
        return "tasks/edit";
    }

    /**
     * タスクを更新する
     */
    @PostMapping("/{id}/update")
    public String updateTask(
            @PathVariable Long id,
            @Validated @ModelAttribute("task") TaskRequest taskRequest,
            BindingResult bindingResult,
            Model model) {
        // 入力エラーがある場合は、編集画面に戻す
        if (bindingResult.hasErrors()) {
            model.addAttribute("taskId", id);
            return "tasks/edit";
        }

        taskService.update(id, taskRequest);
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