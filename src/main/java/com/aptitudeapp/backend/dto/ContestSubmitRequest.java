package com.aptitudeapp.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ContestSubmitRequest {

    private Map<String, Integer> answers;
    private int timeTakenSeconds;
}
