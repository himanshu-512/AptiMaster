package com.aptitudeapp.backend.controller;


import com.aptitudeapp.backend.dto.AuthResponse;
import com.aptitudeapp.backend.dto.SendOtpRequest;
import com.aptitudeapp.backend.dto.VerifyOtpRequest;
import com.aptitudeapp.backend.repository.UserRepository;
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
    private final UserRepository userRepository;

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@Valid @RequestBody SendOtpRequest request) {

        otpService.sendOtp(request.getPhone());

        return ResponseEntity.ok("OTP sent successfully");
    }
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {

        String userId = authentication.getName();
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(Map.of(
                "userId", userId,
                "name", user.getName() != null ? user.getName() : "",
                "role", user.getRole().name(),
                "profileComplete", user.isProfileComplete(),
                "firstLogin", user.isFirstLogin()
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
