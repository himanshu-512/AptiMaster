package com.aptitudeapp.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DailyProgressResponse {
    private int minutes;
    private int goal;
    private int percent;
}