package com.aptitudeapp.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttemptRequest {

    private String questionId;
    private int selectedAnswer;
    private Integer timeSpent; // ✅ ADD THIS
}
