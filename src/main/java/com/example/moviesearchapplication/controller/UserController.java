package com.example.moviesearchapplication.controller;

import com.example.moviesearchapplication.dto.request.*;
import com.example.moviesearchapplication.dto.response.ApiResponse;
import com.example.moviesearchapplication.dto.response.UserProfileResponse;
import com.example.moviesearchapplication.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse> signUp(@Valid @RequestBody SignUpRequestDto signUpDto) {
        return userService.signUp(signUpDto);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody SignInRequestDto signInRequestDto){
        return userService.login(signInRequestDto);
    }

    @PutMapping("/verify-link")
    ResponseEntity<ApiResponse>verifyLink(@RequestBody VerifyTokenDto verifyTokenDto){
        ApiResponse response = userService.verifyLink(verifyTokenDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUserProfile(){
        ApiResponse<UserProfileResponse> apiResponse = userService.getUserProfile();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/change-password")
    ResponseEntity<ApiResponse<String>> changePassword (@Valid @RequestBody PasswordResetDto passwordResetDto) {
        ApiResponse<String> apiResponse = userService.updatePassword(passwordResetDto);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PostMapping("/forgot-password")
    ResponseEntity<ApiResponse<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto forgotPasswordRequestDto) throws IOException {
        ApiResponse<String> response = userService.forgotPassword(forgotPasswordRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/reset-password")
    ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordRequestDto resetPasswordRequestDto) {
        ApiResponse<String> resetPasswordResponse = userService.resetPassword(resetPasswordRequestDto);
        return new ResponseEntity<>(resetPasswordResponse, HttpStatus.CREATED);
    }
}
