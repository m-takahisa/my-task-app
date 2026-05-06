package com.m_takahisa.taskapp.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskPriority {
    CRITICAL("最優先"),
    HIGH("高"),
    MEDIUM("中"),
    LOW("低");

    private final String displayName; // 画面表示用の日本語名
}