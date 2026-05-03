package com.aptitudeapp.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Data
public class AuthResponse {
    private String Token;
    private String userId;
    private String message;
    private boolean profileComplete;
    private boolean firstLogin;
}
