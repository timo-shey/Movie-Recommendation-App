package com.example.moviesearchapplication.controller;

import com.example.moviesearchapplication.dto.request.PasswordResetDto;
import com.example.moviesearchapplication.dto.request.SignInRequestDto;
import com.example.moviesearchapplication.dto.request.SignUpRequestDto;
import com.example.moviesearchapplication.dto.response.ApiResponse;
import com.example.moviesearchapplication.dto.response.AuthResponse;
import com.example.moviesearchapplication.dto.response.UserProfileResponse;
import com.example.moviesearchapplication.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private static final String SIGN_UP_URL = "/api/v1/auth/sign-up";
    private static final String LOGIN_URL = "/api/v1/auth/login";
    private static final String USER_URL = "/api/v1/auth/user";
    private static final String CHANGE_PASSWORD_URL = "/api/v1/auth/change-password";

    @Mock
    UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testSignUp() throws Exception {
        SignUpRequestDto requestDto = new SignUpRequestDto(
                "John", "Doe", "johndoe@example.com", "22233344455", "password123");
        ApiResponse expectedResponse = new ApiResponse(HttpStatus.CREATED.name(), "User registered successfully!", null);
        Mockito.when(userService.signUp(requestDto)).thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.CREATED));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(SIGN_UP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        String jsonResult = result.getResponse().getContentAsString();
        ApiResponse actualResponse = new ObjectMapper().readValue(jsonResult, ApiResponse.class);
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testLogin() throws Exception {
        SignInRequestDto requestDto = new SignInRequestDto("johndoe@example.com", "password123");
        AuthResponse expectedResponse = new AuthResponse("access_token");
        Mockito.when(userService.login(requestDto)).thenReturn(expectedResponse);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String jsonResult = result.getResponse().getContentAsString();
        AuthResponse actualResponse = new ObjectMapper().readValue(jsonResult, AuthResponse.class);
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testGetUserProfile() throws Exception {
        UserProfileResponse expectedResponse = new UserProfileResponse(
                "John", "Doe", "johndoe@example.com", "22233344455");
        ApiResponse<UserProfileResponse> expectedApiResponse = new ApiResponse<>(HttpStatus.OK.name(), "User profile retrieved successfully!", expectedResponse);
        Mockito.when(userService.getUserProfile()).thenReturn(expectedApiResponse);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(USER_URL))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String jsonResult = result.getResponse().getContentAsString();
        ApiResponse<UserProfileResponse> actualApiResponse = new ObjectMapper().readValue(jsonResult, new TypeReference<>(){});
        Assert.assertEquals(expectedApiResponse, actualApiResponse);
    }

    @Test
    public void testChangePassword() throws Exception {
        String oldPassword = "old_password";
        String newPassword = "new_password";

        PasswordResetDto passwordResetDto = new PasswordResetDto();
        passwordResetDto.setCurrentPassword(oldPassword);
        passwordResetDto.setNewPassword(newPassword);
        passwordResetDto.setConfirmNewPassword(newPassword);

        ApiResponse<String> expectedApiResponse = new ApiResponse<>(HttpStatus.CREATED.name(), "Password changed successfully", null);
        Mockito.when(userService.updatePassword(passwordResetDto)).thenReturn(expectedApiResponse);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(CHANGE_PASSWORD_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(passwordResetDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        String jsonResult = result.getResponse().getContentAsString();
        ApiResponse actualApiResponse = new ObjectMapper().readValue(jsonResult, ApiResponse.class);

        Assert.assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        Assert.assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        Assert.assertNull(actualApiResponse.getData());
    }
}