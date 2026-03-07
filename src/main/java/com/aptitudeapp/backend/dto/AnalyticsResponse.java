package com.aptitudeapp.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AnalyticsResponse {

    private int totalQuestions;
    private double accuracy;

    private List<Integer> trend;
    private List<String> labels;

    private double quant;
    private double reasoning;
    private double verbal;

    private String weakTopic;

    private int avgTime;
}