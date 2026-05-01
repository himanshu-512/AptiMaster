package com.aptitudeapp.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ContestSummaryResponse {

    private String id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int durationMinutes;
    private int totalQuestions;
    private String status;
}
