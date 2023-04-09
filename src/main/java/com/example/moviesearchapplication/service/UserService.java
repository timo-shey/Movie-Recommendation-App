package com.example.moviesearchapplication.service;

import com.example.moviesearchapplication.dto.request.PasswordResetDto;
import com.example.moviesearchapplication.dto.request.SignInRequestDto;
import com.example.moviesearchapplication.dto.request.SignUpRequestDto;
import com.example.moviesearchapplication.dto.response.ApiResponse;
import com.example.moviesearchapplication.dto.response.AuthResponse;
import com.example.moviesearchapplication.dto.response.UserProfileResponse;
import com.example.moviesearchapplication.exception.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
@Service
public interface UserService {
    ResponseEntity<ApiResponse> signUp(SignUpRequestDto signUpRequestDto) throws ValidationException;
    AuthResponse login(SignInRequestDto request);
    ApiResponse<String> updatePassword(PasswordResetDto passwordResetDTO);
    ApiResponse<UserProfileResponse> getUserProfile();
}
