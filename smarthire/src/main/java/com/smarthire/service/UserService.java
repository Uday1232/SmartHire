package com.smarthire.service;

import com.smarthire.dto.request.LoginRequest;
import com.smarthire.dto.request.CreateUserRequest;
import com.smarthire.dto.request.RegisterRequest;
import com.smarthire.dto.response.AuthResponse;
import com.smarthire.dto.response.UserResponse;

public interface UserService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest loginRequest);

    UserResponse createInterviewer(CreateUserRequest request);

    java.util.List<UserResponse> getInterviewersForCurrentCompany();
}
