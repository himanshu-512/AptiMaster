package com.aptitudeapp.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ContestQuestionResponse {

    private String id;
    private String topic;
    private String subtopic;
    private String difficulty;
    private String questionText;
    private List<String> options;
}
