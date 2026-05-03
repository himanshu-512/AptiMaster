package com.aptitudeapp.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PracticeRequest {

    private String topic;
    private String subtopic;
    private String difficulty;
    private int count;
}
