package com.m_takahisa.taskapp.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record TaskRequest(
        Long id,
        @NotBlank(message = "タイトルは必須です")
        @Size(max = 100, message = "タイトルは100文字以内で入力してください")
        String title,
        @Size(max = 500, message = "説明は500文字以内で入力してください")
        String description,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
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
                task.getStartDate(),
                task.getDueDate(),
                task.getStatus(),
                task.isCompleted()
        );
    }
}