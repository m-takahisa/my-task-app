package com.m_takahisa.taskapp.exception;

import com.m_takahisa.taskapp.auth.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@ControllerAdvice // アプリ全体のControllerに対して横断的に処理を行う
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    /**
     * UserException（およびその子クラス）が発生した際の共通処理
     */
    @ExceptionHandler(UserException.class)
    public String handleUserException(UserException e,
                                      Model model,
                                      Locale locale) {

        // 例外の種類に応じたメッセージキーを決定
        String messageKey = "user.register.generic_error";
        if (e instanceof UserException.AlreadyExistsException) {
            messageKey = "user.register.email.already_exists";
        }

        // messages.properties からメッセージを取得
        String errorMessage = messageSource.getMessage(messageKey, null, locale);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("user", e.getData());

        return "auth/register";
    }

    /**
     * 通知関連で例外が発生した際の処理
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(ResourceNotFoundException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error/404"; // あるいは共通のエラー画面
    }
}