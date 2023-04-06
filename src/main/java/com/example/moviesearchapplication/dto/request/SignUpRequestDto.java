package com.example.moviesearchapplication.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SignUpRequestDto {
    @NotBlank(message = "firstname cannot be blank")
    private String firstName;

    @NotBlank(message = "lastname cannot bpe blank")
    private String lastName;

    @NotBlank(message = "email cannot be blank")
    @Email(message = "Email must be valid")
    private String email;

    @NotNull(message = "Phone number cannot be null")
    private String phoneNumber;

    @NotBlank(message = "password cannot be blank")
    private String password;
}
