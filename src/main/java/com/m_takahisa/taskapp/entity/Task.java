package com.m_takahisa.taskapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private User user;

    @NotBlank(message = "タイトルは必須です")
    @Size(max = 100, message = "タイトルは100文字以内で入力してください")
    @Column(nullable = false, length = 100, columnDefinition = "VARCHAR(100)") //
    private String title;

    @Size(max = 500, message = "説明は500文字以内で入力してください")
    @Column(columnDefinition = "TEXT") //
    private String description;

    @Column(name = "due_date", columnDefinition = "DATE") //
    private LocalDate dueDate;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'TODO'") //
    private String status = "TODO"; // 初期値

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