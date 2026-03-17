package com.m_takahisa.taskapp.auth;

public class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }

    // 重複エラー用のインナークラス
    public static class AlreadyExistsException extends UserException {
        public AlreadyExistsException(String message) {
            super(message);
        }
    }

//    // 認証エラー用のインナークラス
//    public static class UnauthorizedException extends UserException {
//        public UnauthorizedException(String message) {
//            super(message);
//        }
//    }
}
