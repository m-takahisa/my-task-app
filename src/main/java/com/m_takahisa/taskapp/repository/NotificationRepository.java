package com.m_takahisa.taskapp.repository;

import com.m_takahisa.taskapp.entity.Notification;
import com.m_takahisa.taskapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // 未読(isRead = false)の通知を、作成日時の降順で取得する
    List<Notification> findByIsReadFalseOrderByCreatedAtDesc();

    // 特定のタスクIDに関連する未読通知が存在するかチェック
    boolean existsByTaskIdAndIsReadFalse(Long taskId);
}