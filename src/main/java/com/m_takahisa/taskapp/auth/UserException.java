package com.m_takahisa.taskapp.auth;

public class UserException extends RuntimeException {
    private final Object data;

    // 親クラスのコンストラクタ
    public UserException(String message, Object data) {
        super(message);
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    // 重複エラー用のインナークラス
    public static class AlreadyExistsException extends UserException {
        public AlreadyExistsException(Object data) {
            super("User already exists", data);
        }
    }
}
