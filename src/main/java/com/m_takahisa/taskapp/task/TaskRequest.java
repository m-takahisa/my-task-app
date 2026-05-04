package com.m_takahisa.taskapp.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record TaskRequest(
        Long id,
        @NotBlank(message = "タイトルは必須です")
        String title,
        String description,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @NotNull(message = "期限を設定してください")
        LocalDate dueDate,
        @NotNull(message = "ステータスを選択してください")
        TaskStatus status,
        boolean completed
) {
    // EntityからRequest DTOを作成するメソッドを追加
    public static TaskRequest fromEntity(Task task) {
        return new TaskRequest(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getStatus(),
                task.isCompleted()
        );
    }
}