package com.aptitudeapp.backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Document(collection = "contest_submissions")
@CompoundIndex(name = "contest_user_submission", def = "{'contestId': 1, 'userId': 1}", unique = true)
public class ContestSubmission {

    @Id
    private String id;

    private String contestId;
    private String userId;

    private Map<String, Integer> answers = new HashMap<>();

    private int totalQuestions;
    private int correctAnswers;
    private int score;
    private int timeTakenSeconds;
    private LocalDateTime submittedAt = LocalDateTime.now();
}
