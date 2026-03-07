package com.aptitudeapp.backend.controller;


import com.aptitudeapp.backend.dto.AuthResponse;
import com.aptitudeapp.backend.dto.SendOtpRequest;
import com.aptitudeapp.backend.dto.VerifyOtpRequest;
import com.aptitudeapp.backend.model.User;
import com.aptitudeapp.backend.service.AuthService;
import com.aptitudeapp.backend.service.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final OtpService otpService;

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@Valid @RequestBody SendOtpRequest request) {

        otpService.sendOtp(request.getPhone());

        return ResponseEntity.ok("OTP sent successfully");
    }
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {

        String userId = authentication.getName();

        return ResponseEntity.ok(Map.of(
                "userId", userId
        ));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponse> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) throws Exception {

        AuthResponse response =
                otpService.verifyOtp(request.getPhone(), request.getOtp());

        return ResponseEntity.ok(response);
    }
}