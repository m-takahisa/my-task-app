package com.m_takahisa.taskapp.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record TaskRequest(
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
}