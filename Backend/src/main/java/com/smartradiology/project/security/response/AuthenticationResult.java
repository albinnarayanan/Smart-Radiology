package com.smartradiology.project.security.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseCookie;

@Data
@AllArgsConstructor
public class AuthenticationResult {
    private final UserInfoResponse response;
    private final ResponseCookie accessTokenCookie;
    private final ResponseCookie refreshTokenCookie;

}
