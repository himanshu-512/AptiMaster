package com.aptitudeapp.backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileResponse {

    private String id;
    private String name;
    private Integer age;
    private String avatar;
    private String email;
    private boolean profileComplete;
    private boolean firstLogin;
    private String examGoal;
    private String target;
    private Integer dailyGoal;
    private java.util.List<String> preferredTopics;

    private int totalQuestions;
    private int totalCorrect;
    private int globalScore;
    private int xp;
    private int level;
    private int streak;

    private double accuracy;
}
