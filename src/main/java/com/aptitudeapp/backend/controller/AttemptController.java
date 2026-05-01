package com.aptitudeapp.backend.controller;

import com.aptitudeapp.backend.dto.*;
import com.aptitudeapp.backend.service.AnalyticsService;
import com.aptitudeapp.backend.service.AttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attempt")
@RequiredArgsConstructor
public class AttemptController {

    private final AttemptService attemptService;
    private final AnalyticsService analyticsService;

    @PostMapping("/submit")
    public ResultResponse submitAttempt(
            @RequestBody AttemptSubmissionRequest request) {

        return attemptService.submitAttempt(request);
    }
    @GetMapping("/daily-progress")
    public ResponseEntity<DailyProgressResponse> getDailyProgress(Authentication authentication) {

        String userId = authentication.getName();

        DailyProgressResponse response = analyticsService.getDailyProgress(userId);

        return ResponseEntity.ok(response);
    }
}