package com.smartradiology.project.service;

import com.smartradiology.project.security.request.LoginRequest;
import com.smartradiology.project.security.response.AuthenticationResult;
import org.springframework.http.ResponseCookie;

public interface AuthService {

    AuthenticationResult login(LoginRequest loginRequest);

    ResponseCookie logoutUser(String accessToken, String refreshToken);
}
