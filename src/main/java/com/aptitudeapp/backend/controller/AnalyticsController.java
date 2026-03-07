package com.aptitudeapp.backend.controller;

import com.aptitudeapp.backend.dto.AnalyticsResponse;
import com.aptitudeapp.backend.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/{period}")
    public AnalyticsResponse getAnalytics(
            @PathVariable String period) {

        return analyticsService.getAnalytics(period);
    }
}
