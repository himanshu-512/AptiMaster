package com.aptitudeapp.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeaderboardEntry {

    private int rank;
    private String userId;
    private String name;
    private String avatar;
    private int points;
}