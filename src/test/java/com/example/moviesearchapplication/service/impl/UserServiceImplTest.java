package com.example.moviesearchapplication.service.impl;

import com.example.moviesearchapplication.configuration.JwtUtils;
import com.example.moviesearchapplication.dto.request.SignUpRequestDto;
import com.example.moviesearchapplication.dto.response.ApiResponse;
import com.example.moviesearchapplication.dto.response.UserProfileResponse;
import com.example.moviesearchapplication.enums.Role;
import com.example.moviesearchapplication.exception.ValidationException;
import com.example.moviesearchapplication.model.User;
import com.example.moviesearchapplication.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    public void testSignUp() throws ValidationException {
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .password("password")
                .build();

        when(userRepository.existsByEmail(signUpRequestDto.getEmail())).thenReturn(false);

        User user = User.builder()
                .firstName(signUpRequestDto.getFirstName())
                .lastName(signUpRequestDto.getLastName())
                .email(signUpRequestDto.getEmail())
                .phoneNumber(signUpRequestDto.getPhoneNumber())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .role(Role.ROLE_USER)
                .confirmationToken("token")
                .isActive(true)
                .build();

        when(jwtUtils.generateSignUpConfirmationToken(signUpRequestDto.getEmail())).thenReturn("token");

        UserProfileResponse userProfileResponse = UserProfileResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();

        ApiResponse<UserProfileResponse> expectedResponse = new ApiResponse<>("Successful", "SignUp Successful. Account created successfully", userProfileResponse);

        ResponseEntity<ApiResponse> actualResponse = userServiceImpl.signUp(signUpRequestDto);

        assertEquals(actualResponse.getStatusCode(), HttpStatus.OK);
        assertEquals(actualResponse.getBody(), expectedResponse);
    }
}