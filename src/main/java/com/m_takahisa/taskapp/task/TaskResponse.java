package com.m_takahisa.taskapp.task;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TaskResponse(
        Long id,
        String title,
        TaskPriority priority,
        String priorityDisplayName,
        String description,
        LocalDate startDate,
        LocalDate dueDate,
        TaskStatus status,
        String statusDisplayName,
        boolean completed,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static TaskResponse fromEntity(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getPriority(),
                task.getPriority().getDisplayName(),
                task.getDescription(),
                task.getStartDate(),
                task.getDueDate(),
                task.getStatus(),
                task.getStatus().getDisplayName(),
                task.isCompleted(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}