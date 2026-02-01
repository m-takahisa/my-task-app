package com.m_takahisa.taskapp.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskStatus {
    TODO("未完了"),
    DOING("進行中"),
    DONE("完了");

    private final String displayName; // 画面表示用の日本語名
}