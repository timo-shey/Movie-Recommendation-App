package com.example.moviesearchapplication.controller;

import com.example.moviesearchapplication.dto.request.PasswordResetDto;
import com.example.moviesearchapplication.dto.request.SignInRequestDto;
import com.example.moviesearchapplication.dto.request.SignUpRequestDto;
import com.example.moviesearchapplication.dto.response.ApiResponse;
import com.example.moviesearchapplication.dto.response.AuthResponse;
import com.example.moviesearchapplication.dto.response.UserProfileResponse;
import com.example.moviesearchapplication.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse> signUp(@Valid @RequestBody SignUpRequestDto signUpDto){
        return userService.signUp(signUpDto);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody SignInRequestDto signInRequestDto){
        AuthResponse authResponse = userService.login(signInRequestDto);
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUserProfile(){
        ApiResponse<UserProfileResponse> apiResponse = userService.getUserProfile();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/change-password")
    ResponseEntity<ApiResponse<String>> changePassword (@Valid @RequestBody PasswordResetDto passwordResetDto){
        ApiResponse<String> apiResponse = userService.updatePassword(passwordResetDto);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }
}
