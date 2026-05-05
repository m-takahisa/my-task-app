package com.m_takahisa.taskapp.task;

import com.m_takahisa.taskapp.auth.User;
import com.m_takahisa.taskapp.auth.UserDetailsImpl;
import com.m_takahisa.taskapp.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskListResponse getTaskListData(String keyword, TaskStatus status) {
        List<TaskResponse> tasks = searchTasks(keyword, status);
        // 画面に必要な情報を1つのレコードにまとめる
        return new TaskListResponse(tasks, keyword, status);
    }

    // まとめるためのRecord
    public record TaskListResponse(
            List<TaskResponse> tasks,
            String keyword,
            TaskStatus status
    ) {
    }

    /**
     * タスクの取得処理
     */
    public TaskRequest getTaskRequestById(Long id) {
        return taskRepository.findById(id)
                .map(TaskRequest::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("指定されたタスク（ID: " + id + "）は見つかりません。"));
    }

    /**
     * ログインユーザーの取得
     */
    private User getAuthenticatedUser() {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return principal.getUser();
    }

    /**
     * キーワード、ステータスに基づいてタスクを検索します
     * どちらも指定がない場合は全件取得を返します
     */
    public List<TaskResponse> searchTasks(String keyword, TaskStatus status) {
        boolean hasKeyword = (keyword != null && !keyword.isBlank());
        boolean hasStatus = (status != null);

        User user = getAuthenticatedUser();
        List<Task> tasks;

        if (hasKeyword) {
            if (hasStatus) {
                // キーワード ＋ ステータス絞り込み
                tasks = taskRepository.findByUserAndTitleContainingAndStatus(user, keyword, status);
            } else {
                // キーワードのみ（ステータスは「すべて」）
                tasks = taskRepository.findByUserAndTitleContaining(user, keyword);
            }
        } else if (hasStatus) {
            // ステータス絞り込み
            tasks = taskRepository.findByUserAndStatus(user, status);
        } else {
            // どちらも指定がない場合
            tasks = taskRepository.findByUserOrderByDueDateAsc(user);
        }
        // Entity のリストを TaskResponse のリストに変換して返す
        return tasks.stream()
                .map(TaskResponse::fromEntity)
                .toList();
    }

    /**
     * タスクの登録処理
     */
    @Transactional
    public Task save(TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStartDate(request.startDate());
        task.setDueDate(request.dueDate());
        task.setStatus(request.status());
        task.setCompleted(request.completed());
        task.setUser(getAuthenticatedUser());
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
        task.setStartDate(request.startDate());
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

}