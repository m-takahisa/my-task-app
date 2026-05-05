package com.m_takahisa.taskapp.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskStatus {
    TODO("未着手"),
    DOING("進行中"),
    DONE("完了"),
    PENDING("保留中");

    private final String displayName; // 画面表示用の日本語名
}