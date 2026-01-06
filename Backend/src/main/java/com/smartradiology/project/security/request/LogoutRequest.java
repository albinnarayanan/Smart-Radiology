package com.smartradiology.project.security.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LogoutRequest {
    @NotBlank(message = "Refresh token is required for proper logout")
    private String refreshToken;
}
