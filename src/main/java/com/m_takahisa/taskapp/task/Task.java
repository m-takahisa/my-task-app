package com.m_takahisa.taskapp.task;

import com.m_takahisa.taskapp.auth.User;
import com.m_takahisa.taskapp.task.notification.Notification;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tasks")
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT") //
    private Long id;

    // Userとの「多対一」のリレーション設定
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BIGINT") //
    @ToString.Exclude // デバック時のエラー対策（動作には影響しない）
    private User user;

    // 通知テーブルとのリレーション設定
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;

    @NotBlank(message = "タイトルは必須です")
    @Size(max = 100, message = "タイトルは100文字以内で入力してください")
    @Column(nullable = false, length = 100, columnDefinition = "VARCHAR(100)") //
    private String title;

    @Size(max = 500, message = "説明は500文字以内で入力してください")
    @Column(columnDefinition = "TEXT") //
    private String description;

    @Column(name = "start_date", columnDefinition = "DATE") //
    private LocalDate startDate;

    @Column(name = "due_date", columnDefinition = "DATE") //
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TaskStatus status = TaskStatus.TODO;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE") //
    private boolean completed = false;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}