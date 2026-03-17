package com.m_takahisa.taskapp.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

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
                               BindingResult bindingResult) {
        // 入力エラーがある場合は、登録画面に戻す
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        userService.registerUser(request);
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
