package com.aptitudeapp.backend.service;

import com.aptitudeapp.backend.dto.ProfileResponse;
import com.aptitudeapp.backend.dto.ProfileUpdateRequest;
import com.aptitudeapp.backend.model.User;
import com.aptitudeapp.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ProfileResponse getProfile(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        double accuracy = user.getTotalQuestions() == 0
                ? 0
                : ((double) user.getTotalCorrect() / user.getTotalQuestions()) * 100;

        return ProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .age(user.getAge())
                .avatar(user.getAvatar())
                .email(user.getEmail())
                .profileComplete(user.isProfileComplete())
                .firstLogin(user.isFirstLogin())
                .examGoal(user.getExamGoal())
                .target(user.getTarget())
                .dailyGoal(user.getDailyGoal())
                .preferredTopics(user.getPreferredTopics())
                .totalQuestions(user.getTotalQuestions())
                .totalCorrect(user.getTotalCorrect())
                .globalScore(user.getGlobalScore())
                .xp(user.getXp())
                .level(user.getLevel())
                .streak(user.getStreak())
                .accuracy(accuracy)
                .build();
    }

    public ProfileResponse updateProfile(String userId, ProfileUpdateRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getName() != null)
            user.setName(request.getName());

        if (request.getAge() != null)
            user.setAge(request.getAge());

        if (request.getAvatar() != null)
            user.setAvatar(request.getAvatar());

        if (request.getEmail() != null)
            user.setEmail(request.getEmail());

        if (request.getExamGoal() != null)
            user.setExamGoal(request.getExamGoal());

        if (request.getTarget() != null)
            user.setTarget(request.getTarget());

        if (request.getDailyGoal() != null)
            user.setDailyGoal(request.getDailyGoal());

        if (request.getPreferredTopics() != null)
            user.setPreferredTopics(request.getPreferredTopics());

        if (hasText(user.getName())) {
            user.setProfileComplete(true);
            user.setFirstLogin(false);
        }

        userRepository.save(user);

        return getProfile(userId);
    }

    public void updateUserStreak(User user) {

        LocalDate today = LocalDate.now();
        LocalDate lastDate = user.getLastPracticeDate();

        if (lastDate == null) {
            user.setStreak(1);
        }

        else if (lastDate.equals(today.minusDays(1))) {
            user.setStreak(user.getStreak() + 1);
        }

        else if (lastDate.equals(today)) {
            return;
        }

        else {
            user.setStreak(1);
        }

        user.setLastPracticeDate(today);
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
