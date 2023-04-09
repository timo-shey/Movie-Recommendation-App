package com.example.moviesearchapplication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetDto {
    @NotNull(message = "Password must not be empty")
    private String currentPassword;
    @NotNull(message = "Password must not be empty")
    private String newPassword;
    @NotNull(message = "Password must not be empty")
    private String confirmNewPassword;
}
