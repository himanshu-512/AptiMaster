package com.aptitudeapp.backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "contests")
public class Contest {

    @Id
    private String id;

    private String title;
    private String description;

    @Indexed
    private LocalDateTime startTime;

    @Indexed
    private LocalDateTime endTime;

    private int durationMinutes;
    private int totalQuestions;
    private int pointsPerQuestion = 10;
    private List<String> questionIds = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();
}
