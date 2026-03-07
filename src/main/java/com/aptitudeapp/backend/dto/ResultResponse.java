package com.aptitudeapp.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResultResponse {

    private int totalQuestions;
    private int correctAnswers;
    private double accuracy;
    private int score;
}
