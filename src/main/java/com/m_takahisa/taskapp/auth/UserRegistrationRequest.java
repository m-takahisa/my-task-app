package com.m_takahisa.taskapp.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationRequest(
        @Size(min = 1, max = 20, message = "{validation.size.min_max}")
        String username,

        @NotBlank(message = "{validation.not_blank}")
        @Email(message = "{validation.email.invalid}")
        String email,

        @Size(min = 8, max = 20, message = "{validation.size.min_max}")
        String password
) {
}
