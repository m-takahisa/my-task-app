package com.m_takahisa.taskapp.task;

import com.m_takahisa.taskapp.task.notification.NotificationService;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
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
    private final NotificationService notificationService;

    /**
     * 検索を行う
     */
    @GetMapping
    public String listTasks(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "status", required = false) TaskStatus status,
            Model model) {

        var response = taskService.getTaskListData(keyword, status);
        model.addAttribute("displayData", response);
        return "tasks/list";
    }

    /**
     * 登録画面を表示する
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute(
                "task",
                new TaskRequest(
                        null,
                        "",
                        TaskPriority.LOW,
                        null,
                        null,
                        null,
                        TaskStatus.TODO,
                        false
                )
        );
        return "tasks/create";
    }

    /**
     * 保存処理を行う
     */
    @PostMapping("/create")
    public String createTask(@Validated @ModelAttribute("task") TaskRequest taskRequest,
                             BindingResult bindingResult) {
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
        model.addAttribute("task", taskService.getTaskRequestById(id));
        model.addAttribute("taskId", id);
        return "tasks/edit";
    }

    /**
     * タスクを更新する
     */
    @PostMapping("/{id}/update")
    public String updateTask(
            @PathVariable Long id,
            @Validated({Default.class, OnUpdate.class}) @ModelAttribute("task") TaskRequest taskRequest,
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
        notificationService.markAsRead(id);
        return "redirect:/view/tasks";
    }
}