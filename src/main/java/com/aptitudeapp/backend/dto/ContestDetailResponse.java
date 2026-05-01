package com.aptitudeapp.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ContestDetailResponse {

    private String id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int durationMinutes;
    private int totalQuestions;
    private String status;
    private long serverTime;
    private boolean submitted;
    private List<ContestQuestionResponse> questions;
}
