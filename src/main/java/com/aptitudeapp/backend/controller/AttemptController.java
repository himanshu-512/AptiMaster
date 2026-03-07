package com.aptitudeapp.backend.controller;

import com.aptitudeapp.backend.dto.*;
import com.aptitudeapp.backend.service.AttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attempt")
@RequiredArgsConstructor
public class AttemptController {

    private final AttemptService attemptService;

    @PostMapping("/submit")
    public ResultResponse submitAttempt(
            @RequestBody AttemptSubmissionRequest request) {

        return attemptService.submitAttempt(request);
    }
}