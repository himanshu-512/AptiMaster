package com.aptitudeapp.backend.service;

import com.aptitudeapp.backend.dto.AnalyticsResponse;
import com.aptitudeapp.backend.model.Attempt;
import com.aptitudeapp.backend.repository.AttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final AttemptRepository attemptRepository;

    public AnalyticsResponse getAnalytics(String period) {

        String userId =
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();

        List<Attempt> attempts;

        if (period.equals("30")) {
            attempts = attemptRepository
                    .findByUserIdAndAttemptedAtAfter(
                            userId,
                            LocalDateTime.now().minusDays(30)
                    );
        }
        else if (period.equals("90")) {
            attempts = attemptRepository
                    .findByUserIdAndAttemptedAtAfter(
                            userId,
                            LocalDateTime.now().minusDays(90)
                    );
        }
        else {
            attempts = attemptRepository.findByUserId(userId);
        }

        int total = attempts.size();

        int correct = (int) attempts.stream()
                .filter(Attempt::isCorrect)
                .count();

        double accuracy = total == 0
                ? 0
                : ((double) correct / total) * 100;

        // -----------------------------
        // Daily trend
        // -----------------------------

        Map<LocalDate, Integer> trendMap = new TreeMap<>();

        for (Attempt attempt : attempts) {

            LocalDate date =
                    attempt.getAttemptedAt().toLocalDate();

            trendMap.put(
                    date,
                    trendMap.getOrDefault(date, 0) + 1
            );
        }

        List<Integer> trend = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (Map.Entry<LocalDate, Integer> entry : trendMap.entrySet()) {
            trend.add(entry.getValue());
            labels.add(entry.getKey().getDayOfWeek().name().substring(0,3));
        }

        // -----------------------------
        // Topic Accuracy
        // -----------------------------

        Map<String, Integer> topicTotal = new HashMap<>();
        Map<String, Integer> topicCorrect = new HashMap<>();

        for (Attempt a : attempts) {

            String topic = a.getTopic();

            topicTotal.put(
                    topic,
                    topicTotal.getOrDefault(topic, 0) + 1
            );

            if (a.isCorrect()) {
                topicCorrect.put(
                        topic,
                        topicCorrect.getOrDefault(topic, 0) + 1
                );
            }
        }

        double quant = calculateAccuracy(topicTotal, topicCorrect, "Quantitative");
        double reasoning = calculateAccuracy(topicTotal, topicCorrect, "Logical Reasoning");
        double verbal = calculateAccuracy(topicTotal, topicCorrect, "Verbal Ability");

        // -----------------------------
        // Weak Topic
        // -----------------------------

        String weakTopic = null;
        double lowest = 100;

        for (String topic : topicTotal.keySet()) {

            double acc = calculateAccuracy(topicTotal, topicCorrect, topic);

            if (acc < lowest) {
                lowest = acc;
                weakTopic = topic;
            }
        }

        // -----------------------------
        // Average Time
        // -----------------------------

        int avgTime = attempts.isEmpty()
                ? 0
                : (int) attempts.stream()
                .mapToInt(Attempt::getTimeTaken)
                .average()
                .orElse(0);

        return new AnalyticsResponse(
                total,
                accuracy,
                trend,
                labels,
                quant,
                reasoning,
                verbal,
                weakTopic,
                avgTime
        );
    }

    private double calculateAccuracy(
            Map<String,Integer> total,
            Map<String,Integer> correct,
            String topic
    ){

        int t = total.getOrDefault(topic,0);
        int c = correct.getOrDefault(topic,0);

        if(t == 0) return 0;

        return ((double)c/t)*100;
    }
}