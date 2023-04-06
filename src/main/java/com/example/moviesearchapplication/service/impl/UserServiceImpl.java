package com.example.moviesearchapplication.service.impl;

import com.example.moviesearchapplication.configuration.mail.EmailService;
import com.example.moviesearchapplication.configuration.security.CustomUserDetailService;
import com.example.moviesearchapplication.configuration.security.JwtUtils;
import com.example.moviesearchapplication.dto.request.*;
import com.example.moviesearchapplication.dto.response.ApiResponse;
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

import java.io.IOException;
import java.util.Optional;

@Data
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
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
                .password(signUpRequestDto.getPassword())
                .role(Role.ROLE_USER)
                .confirmationToken(token)
//                .ratingId(null)
                .build();
        userRepository.save(user);
        String URL = "http://localhost:8080/api/v1/auth/verify-link/?token=" + token;
        String link = "<h3>Hello "  + signUpRequestDto.getFirstName()  +"<br> Click the link below to activate your account <a href=" + URL + "><br>Activate</a></h3>";

        emailService.sendEmail(signUpRequestDto.getEmail(),"MovieSearch: Verify Your Account", link);

        return ResponseEntity.ok(new ApiResponse<>("Successful", "SignUp Successful. Check your mail to activate your account", null));
    }

    @Override
    public ResponseEntity<String> login(SignInRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail()).
                orElseThrow(
                        ()-> new UserNotFoundException("User Not Found")
                );
        if(!user.isActive()) throw new ValidationException("User Not Active");
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UserDetails userDetails = customUserDetailService.loadUserByUsername(request.getEmail());
        if (userDetails != null) {
            return ResponseEntity.ok(jwtUtils.generateToken(userDetails));
        }
        return ResponseEntity.status(400).body("Some Error Occurred");
    }

    @Override
    public ApiResponse verifyLink(VerifyTokenDto verifyTokenDto) {
        Optional<User> existingUser = userRepository.findByConfirmationToken(verifyTokenDto.getToken());
        if (existingUser.isPresent()) {
            if (existingUser.get().isActive()){
                return ApiResponse.builder().message("Account already verified").status("false").build();
            }
            existingUser.get().setConfirmationToken(null);
            existingUser.get().setActive(true);
            userRepository.save(existingUser.get());
            return ApiResponse.builder().message("Success").status("Account created successfully").build();
        }
        throw new UserNotFoundException("Error: No Account found! or Invalid Token");
    }

    @Override
    public ApiResponse<String> updatePassword(PasswordResetDto passwordResetDto) {
        String currentPassword = passwordResetDto.getCurrentPassword();
        String newPassword = passwordResetDto.getNewPassword();

        User user = appUtil.getLoggedInUser();

        String savedPassword = user.getPassword();

        if(!passwordEncoder.matches(currentPassword, savedPassword))
            throw new InvalidCredentialsException("Credentials must match");
        else
            passwordResetDto.setNewPassword(newPassword);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        emailService.sendEmail(user.getEmail(), "Update Password", "Your password has been updated " +
                "successfully. Ensure to keep it a secret. Never disclose your password to a third party.");
        return new ApiResponse<>( "Success", "Password reset successful", null);
    }

    @Override
    public ApiResponse forgotPassword(ForgotPasswordRequestDto forgotPasswordDTORequest) throws IOException {
        String email = forgotPasswordDTORequest.getEmail();

        Boolean isEmailExist = userRepository.existsByEmail(email);
        if (!isEmailExist)
            throw new UserNotFoundException("User Does Not Exist!");

        User user = userRepository.findByEmail(email).get();
        String token = jwtUtils.resetPasswordToken(email);
        user.setConfirmationToken(token);
        userRepository.save(user);

        String resetPasswordLink = "http://localhost:8080/api/v1/auth/resetPassword" + token;
        String resetLink = "<h3>Hello " + user.getFirstName() + ",<br> Click the link below to reset your password <a href=" + resetPasswordLink + "><br>Reset Password</a></h3>";

        emailService.sendEmail(email, "MovieSearch: Click on the link to reset your Password", resetLink);
        return new ApiResponse<>(null, "A password reset link has been sent to your email", null);
    }

    @Override
    public ApiResponse<String> resetPassword(ResetPasswordRequestDto resetPasswordRequestDTO) {
        String password = resetPasswordRequestDTO.getNewPassword();
        User user = userRepository.findByConfirmationToken(resetPasswordRequestDTO.getToken()).get();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return new ApiResponse<String>("Success", "password reset successful", null);
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
