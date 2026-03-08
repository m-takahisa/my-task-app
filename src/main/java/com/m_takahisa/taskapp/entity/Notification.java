package com.m_takahisa.taskapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
public class Notification {

    // どのタスクに関する通知かを確認するために紐付け
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    // Notifications
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 誰宛の通知か

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private boolean isRead = false; // 未読・既読フラグ

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}