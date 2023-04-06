package com.example.moviesearchapplication.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ResetPasswordRequestDto {
    @NotNull(message = "New password must not be empty")
    private String newPassword;
    private String token;
}
