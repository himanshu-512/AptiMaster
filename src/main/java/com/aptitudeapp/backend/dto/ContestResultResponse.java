package com.aptitudeapp.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContestResultResponse {

    private String contestId;
    private int totalQuestions;
    private int correctAnswers;
    private int score;
    private int timeTakenSeconds;
    private int rank;
}
