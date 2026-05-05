package com.m_takahisa.taskapp.task;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record TaskRequest(
        Long id,
        @NotBlank(message = "{validation.not_blank}")
        @Size(max = 100, message = "{validation.size.max}")
        String title,
        @Size(max = 500, message = "{validation.size.max}")
        String description,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate dueDate,
        // 更新時のみ NotNull チェック
        @NotNull(groups = OnUpdate.class)
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

    // 日付の前後関係チェック
    @AssertTrue(message = "{validation.date_order}")
    public boolean isDateOrderValid() {
        if (startDate != null && dueDate != null) {
            return !startDate.isAfter(dueDate);
        }
        return true;
    }

    // タスク完了時のチェック
    @AssertTrue(message = "{validation.status_completed_mismatch}", groups = OnUpdate.class)
    public boolean isStatusDoneValid() {
        // statusがDONEであることと、completedがtrueであることが一致すればOK
        return (this.status == TaskStatus.DONE) == this.completed;
    }
}