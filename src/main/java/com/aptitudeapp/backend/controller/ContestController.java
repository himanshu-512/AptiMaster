package com.aptitudeapp.backend.controller;

import com.aptitudeapp.backend.dto.*;
import com.aptitudeapp.backend.service.ContestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contests")
@RequiredArgsConstructor
public class ContestController {

    private final ContestService contestService;

    @GetMapping
    public List<ContestSummaryResponse> list(
            @RequestParam(defaultValue = "upcoming") String type,
            Authentication authentication
    ) {
        return contestService.list(type, authentication.getName());
    }

    @PostMapping("/{contestId}/register")
    public ContestRegistrationResponse register(
            @PathVariable String contestId,
            Authentication authentication
    ) {
        return contestService.register(contestId, authentication.getName());
    }

    @GetMapping("/{contestId}")
    public ContestDetailResponse detail(
            @PathVariable String contestId,
            Authentication authentication
    ) {
        return contestService.detail(contestId, authentication.getName());
    }

    @PostMapping("/{contestId}/submit")
    public ContestResultResponse submit(
            @PathVariable String contestId,
            @RequestBody ContestSubmitRequest request,
            Authentication authentication
    ) {
        return contestService.submit(contestId, authentication.getName(), request);
    }

    @GetMapping("/{contestId}/leaderboard")
    public List<ContestRankEntry> leaderboard(@PathVariable String contestId) {
        return contestService.leaderboard(contestId);
    }
}
