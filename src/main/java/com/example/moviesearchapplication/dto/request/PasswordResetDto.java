package com.example.moviesearchapplication.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PasswordResetDto {
    @NotNull(message = "Password must not be empty")
    private String currentPassword;

    @NotNull(message = "Password must not be empty")
    private String newPassword;
}
