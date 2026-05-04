package com.m_takahisa.taskapp.exception;

/**
 * 特定のリソース（タスクなど）が見つからない場合に投げる共通例外
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
