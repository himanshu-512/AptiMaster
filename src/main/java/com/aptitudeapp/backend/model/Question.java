package com.aptitudeapp.backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Document(collection = "questions")
@CompoundIndex(name = "topic_difficulty_idx",
        def = "{'topic':1, 'difficulty':1}")
public class Question {

    @Id
    private String id;

    private String topic;
    private String subtopic;

    private Difficulty difficulty;

    private String questionText;

    private List<String> options;

    private int correctAnswer;

    private String explanation;

    private String source;

    private String contestId;

    private LocalDateTime createdAt = LocalDateTime.now();
}
