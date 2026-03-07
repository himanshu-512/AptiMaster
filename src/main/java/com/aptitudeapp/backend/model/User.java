package com.aptitudeapp.backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String name;

    private String phoneHash;
    private String phoneEncrypted;

    private Integer age;

    private String avatar;

    private String email;

    // Practice statistics
    private int totalQuestions = 0;
    private int totalCorrect = 0;
    private int globalScore = 0;

    // Streak system
    private int streak = 0;

    private LocalDate lastPracticeDate;

    private Role role = Role.USER;

    private LocalDateTime createdAt = LocalDateTime.now();
}