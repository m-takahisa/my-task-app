package com.m_takahisa.taskapp.task;

import java.time.LocalDate;

public record TaskResponse(
        Long id,
        String title,
        String description,
        LocalDate dueDate,
        TaskStatus status,
        String statusDisplayName,
        boolean completed
) {
    public static TaskResponse fromEntity(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getStatus(),
                task.getStatus().getDisplayName(),
                task.isCompleted()
        );
    }
}