package com.example.moviesearchapplication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequestDto {
    @NotBlank(message = "Firstname cannot be blank")
    private String firstName;
    @NotBlank(message = "Lastname cannot bpe blank")
    private String lastName;
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be valid")
    private String email;
    @NotNull(message = "Phone number cannot be null")
    private String phoneNumber;
    @NotBlank(message = "Password cannot be blank")
    private String password;
}
