package com.m_takahisa.taskapp.repository;

import com.m_takahisa.taskapp.entity.Task;
import com.m_takahisa.taskapp.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // タイトル部分一致検索
    List<Task> findByTitleContaining(String keyword);

    // ステータスによる完全一致検索
    List<Task> findByStatus(TaskStatus status);

    // タイトルとステータスの組み合わせ検索
    List<Task> findByTitleContainingAndStatus(String keyword, TaskStatus status);

    /**
     * 指定した日付以前（期限切れ含む）かつ、ステータスが完了(DONE)以外のタスクを取得
     */
    List<Task> findByDueDateLessThanEqualAndStatusNot(LocalDate date, TaskStatus status);
}