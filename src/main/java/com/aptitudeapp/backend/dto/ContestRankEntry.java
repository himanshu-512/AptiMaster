package com.aptitudeapp.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContestRankEntry {

    private int rank;
    private String userId;
    private String name;
    private String avatar;
    private int score;
    private int correctAnswers;
    private int timeTakenSeconds;
}
