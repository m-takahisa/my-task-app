package com.m_takahisa.taskapp.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TaskRequest(
        @NotBlank(message = "タイトルは必須です")
        String title,

        String description,

        @NotNull(message = "期限を設定してください")
        LocalDate dueDate,

        @NotNull(message = "ステータスを選択してください")
        TaskStatus status,

        boolean completed
) {
}