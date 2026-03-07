package com.aptitudeapp.backend.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class QuestionResponse {

    private String id;
    private String topic;
    private String difficulty;
    private String questionText;
    private List<String> options;

    // ADD THESE
    private int correctAnswer;
    private String explanation;
}