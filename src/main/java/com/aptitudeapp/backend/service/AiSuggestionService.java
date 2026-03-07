package com.aptitudeapp.backend.service;

import com.aptitudeapp.backend.dto.AiSuggestionResponse;
import com.aptitudeapp.backend.model.Attempt;
import com.aptitudeapp.backend.repository.AttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AiSuggestionService {

    private final AttemptRepository attemptRepository;

    public AiSuggestionResponse getSuggestion() {

        String userId =
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();

        List<Attempt> attempts =
                attemptRepository.findByUserId(userId);

        if (attempts.isEmpty()) {
            return new AiSuggestionResponse("Quantitative", 0);
        }

        Map<String, Integer> totalMap = new HashMap<>();
        Map<String, Integer> correctMap = new HashMap<>();

        for (Attempt attempt : attempts) {

            String topic = attempt.getTopic();

            totalMap.put(topic,
                    totalMap.getOrDefault(topic, 0) + 1);

            if (attempt.isCorrect()) {
                correctMap.put(topic,
                        correctMap.getOrDefault(topic, 0) + 1);
            }
        }

        String weakestTopic = null;
        double lowestAccuracy = 100;

        for (String topic : totalMap.keySet()) {

            int total = totalMap.get(topic);
            int correct = correctMap.getOrDefault(topic, 0);

            double accuracy = ((double) correct / total) * 100;

            if (accuracy < lowestAccuracy) {
                lowestAccuracy = accuracy;
                weakestTopic = topic;
            }
        }

        return new AiSuggestionResponse(
                weakestTopic,
                lowestAccuracy
        );
    }
}