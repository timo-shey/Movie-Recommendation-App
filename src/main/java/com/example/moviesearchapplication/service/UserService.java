package com.example.moviesearchapplication.service;

import com.example.moviesearchapplication.dto.request.*;
import com.example.moviesearchapplication.dto.response.ApiResponse;
import com.example.moviesearchapplication.dto.response.UserProfileResponse;
import com.example.moviesearchapplication.exception.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
public interface UserService {
    ResponseEntity<ApiResponse> signUp(SignUpRequestDto signUpRequestDto) throws ValidationException;
    ResponseEntity<String> login(SignInRequestDto request);
    ApiResponse verifyLink(VerifyTokenDto verifyTokenDto);
    ApiResponse<String> updatePassword(PasswordResetDto passwordResetDTO);
    ApiResponse forgotPassword(ForgotPasswordRequestDto forgotPasswordDTORequest) throws IOException;
    ApiResponse<String> resetPassword(ResetPasswordRequestDto resetPasswordRequestDTO);
    ApiResponse<UserProfileResponse> getUserProfile();

}
