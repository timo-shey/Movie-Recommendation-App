package com.example.moviesearchapplication.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ForgotPasswordRequestDto {
    @NotNull(message = "Email must not be empty")
    private String email;
}
