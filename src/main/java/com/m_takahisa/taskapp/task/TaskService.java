package com.m_takahisa.taskapp.task;

import com.m_takahisa.taskapp.auth.User;
import com.m_takahisa.taskapp.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

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
    public List<Task> searchTasks(String keyword, TaskStatus status) {
        boolean hasKeyword = (keyword != null && !keyword.isBlank());
        boolean hasStatus = (status != null);

        User user = getAuthenticatedUser();

        if (hasKeyword) {
            if (hasStatus) {
                // キーワード ＋ ステータス絞り込み
                return taskRepository.findByUserAndTitleContainingAndStatus(user, keyword, status);
            }
            // キーワードのみ（ステータスは「すべて」）
            return taskRepository.findByUserAndTitleContaining(user, keyword);
        } else if (hasStatus) {
            // ステータス絞り込み
            return taskRepository.findByUserAndStatus(user, status);
        } else {
            // どちらも指定がない場合
            return taskRepository.findByUserOrderByDueDateAsc(user);
        }
    }

    /**
     * タスクの登録処理
     */
    @Transactional
    public Task save(TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
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