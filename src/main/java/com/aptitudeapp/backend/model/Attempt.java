package com.aptitudeapp.backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "attempts")
@CompoundIndex(name = "user_attempt_index", def = "{'userId': 1, 'attemptedAt': -1}")
public class Attempt {

    @Id
    private String id;

    private String userId;
    private String questionId;

    private int selectedAnswer;
    private boolean correct;

    private String topic;

    private String difficulty;

    private int timeTaken;

    private LocalDateTime attemptedAt = LocalDateTime.now();
}