package com.aptitudeapp.backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    private boolean profileComplete = false;
    private boolean firstLogin = true;
    private String examGoal;
    private String target;
    private Integer dailyGoal;
    private List<String> preferredTopics;

    // 📊 Practice statistics
    private int totalQuestions = 0;
    private int totalCorrect = 0;

    @Indexed
    private int globalScore;

    @Indexed
    private int weeklyScore = 0;

    @Indexed
    private int dailyScore = 0;

    // 🔥 Gamification
    @Indexed
    private int xp = 0;

    private int level = 1;

    // 🔥 Streak system
    private int streak = 0;
    private LocalDate lastPracticeDate;

    private Role role = Role.USER;

    private LocalDateTime createdAt = LocalDateTime.now();

    // ✅ Derived field (no DB storage)
    public double getAccuracy() {
        if (totalQuestions == 0) return 0;
        return (totalCorrect * 100.0) / totalQuestions;
    }
}
