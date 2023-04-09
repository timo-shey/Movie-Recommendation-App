package com.example.moviesearchapplication.service.impl;

import com.example.moviesearchapplication.configuration.CustomUserDetailService;
import com.example.moviesearchapplication.configuration.JwtUtils;
import com.example.moviesearchapplication.dto.request.PasswordResetDto;
import com.example.moviesearchapplication.dto.request.SignInRequestDto;
import com.example.moviesearchapplication.dto.request.SignUpRequestDto;
import com.example.moviesearchapplication.dto.response.ApiResponse;
import com.example.moviesearchapplication.dto.response.AuthResponse;
import com.example.moviesearchapplication.dto.response.UserProfileResponse;
import com.example.moviesearchapplication.enums.Role;
import com.example.moviesearchapplication.exception.InvalidCredentialsException;
import com.example.moviesearchapplication.exception.UserNotFoundException;
import com.example.moviesearchapplication.exception.ValidationException;
import com.example.moviesearchapplication.model.User;
import com.example.moviesearchapplication.repository.RatingRepository;
import com.example.moviesearchapplication.repository.UserRepository;
import com.example.moviesearchapplication.service.UserService;
import com.example.moviesearchapplication.utils.AppUtil;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Data
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailService customUserDetailService;
    private final JwtUtils jwtUtils;
    private final AppUtil appUtil;
    private final RatingRepository ratingRepository;

    @Override
    public ResponseEntity<ApiResponse> signUp(SignUpRequestDto signUpRequestDto) throws ValidationException {
        Boolean isUserExist = userRepository.existsByEmail(signUpRequestDto.getEmail());
        if (isUserExist)
            throw new ValidationException("User Already Exists!");
        String token = jwtUtils.generateSignUpConfirmationToken(signUpRequestDto.getEmail());
        User user = User.builder()
                .firstName(signUpRequestDto.getFirstName())
                .lastName(signUpRequestDto.getLastName())
                .email(signUpRequestDto.getEmail())
                .phoneNumber(signUpRequestDto.getPhoneNumber())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .role(Role.ROLE_USER)
                .confirmationToken(token)
                .isActive(true)
                .build();
        userRepository.save(user);
        UserProfileResponse userProfileResponse = UserProfileResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
        return ResponseEntity.ok(new ApiResponse<>(
                "Successful", "SignUp Successful. Account created successfully", userProfileResponse
        ));
    }

    @Override
    public AuthResponse login(SignInRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail()).
                orElseThrow(
                        ()-> new UserNotFoundException("User Not Found")
                );
        if(!user.isActive()) throw new ValidationException("User Not Active");
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UserDetails userDetails = customUserDetailService.loadUserByUsername(request.getEmail());
        String token = jwtUtils.generateToken(userDetails);
        user.setConfirmationToken(null);
        userRepository.save(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public ApiResponse<String> updatePassword(PasswordResetDto passwordResetDto) {
        String currentPassword = passwordResetDto.getCurrentPassword();
        String newPassword = passwordResetDto.getNewPassword();
        String confirmNewPassword = passwordResetDto.getConfirmNewPassword();

        User user = appUtil.getLoggedInUser();

        String savedPassword = user.getPassword();

        if (!newPassword.matches(confirmNewPassword))
            throw new InvalidCredentialsException("Passwords must match");

        if(!passwordEncoder.matches(currentPassword, savedPassword))
            throw new InvalidCredentialsException("Credentials must match");
        else
            passwordResetDto.setNewPassword(newPassword);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return new ApiResponse<>( "Success", "Password Update Successful", null);
    }

    @Override
    public ApiResponse<UserProfileResponse> getUserProfile() {
        User user = appUtil.getLoggedInUser();

        UserProfileResponse response = UserProfileResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();

        return new ApiResponse<>("SUCCESS", "User Profile", response);
    }
}
