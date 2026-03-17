package com.m_takahisa.taskapp.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Locale;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final MessageSource messageSource;

    /**
     * アカウント画面を表示する
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationRequest("", "", ""));
        return "auth/register";
    }

    /**
     * アカウント作成後の処理
     * ログイン画面への遷移
     */
    @PostMapping("/register")
    public String registerUser(@Validated @ModelAttribute("user") UserRegistrationRequest request,
                               BindingResult bindingResult,
                               Locale locale) {
        // 入力エラーがある場合は、登録画面に戻す
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            userService.registerUser(request, locale);
        } catch (UserException.AlreadyExistsException e) {
            // メールアドレスの重複エラーの場合
            String errorMessage = messageSource.getMessage(
                    "user.register.duplicate_field",
                    new Object[]{"メールアドレス", request.email()},
                    locale
            );
            bindingResult.rejectValue("email", "error.user", errorMessage);
            return "auth/register";
        } catch (UserException e) {
            // それ以外のUser関連エラー
            String genericMessage = messageSource.getMessage("user.register.generic_error", null, locale);
            bindingResult.reject("error.user", genericMessage);
            return "auth/register";
        }

        return "redirect:/login";
    }

    /**
     * 編集ログイン画面を表示する
     */
    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }
}
