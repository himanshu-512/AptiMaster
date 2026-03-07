package com.aptitudeapp.backend.service;

import com.aptitudeapp.backend.dto.*;
import com.aptitudeapp.backend.model.*;
import com.aptitudeapp.backend.repository.*;
import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AttemptService {

    private final QuestionRepository questionRepository;
    private final AttemptRepository attemptRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public ResultResponse submitAttempt(AttemptSubmissionRequest request) {

        String userId =
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();

        List<String> questionIds = request.getAttempts()
                .stream()
                .map(AttemptRequest::getQuestionId)
                .toList();

        List<Question> questions =
                questionRepository.findAllById(questionIds);

        Map<String, Question> questionMap = new HashMap<>();

        for (Question q : questions) {
            questionMap.put(q.getId(), q);
        }

        int correctCount = 0;
        List<Attempt> attemptEntities = new ArrayList<>();

        for (AttemptRequest dto : request.getAttempts()) {

            Question question = questionMap.get(dto.getQuestionId());

            boolean isCorrect =
                    question.getCorrectAnswer() == dto.getSelectedAnswer();

            if (isCorrect) correctCount++;

            Attempt attempt = new Attempt();
            attempt.setUserId(userId);
            attempt.setQuestionId(question.getId());
            attempt.setSelectedAnswer(dto.getSelectedAnswer());
            attempt.setCorrect(isCorrect);
            attempt.setTopic(question.getTopic());
            attempt.setDifficulty(question.getDifficulty().name());
            attempt.setTimeTaken(dto.getTimeTaken());

            attemptEntities.add(attempt);
        }

        attemptRepository.saveAll(attemptEntities);

        int total = request.getAttempts().size();
        int score = correctCount * 10;

        int finalCorrectCount = correctCount;

        userRepository.findById(userId).ifPresent(user -> {

            user.setTotalQuestions(user.getTotalQuestions() + total);
            user.setTotalCorrect(user.getTotalCorrect() + finalCorrectCount);

            user.setGlobalScore(user.getGlobalScore() + score);
            user.setWeeklyScore(user.getWeeklyScore() + score);
            user.setDailyScore(user.getDailyScore() + score);

            userService.updateUserStreak(user);

            userRepository.save(user);
        });

        double accuracy = (double) correctCount / total * 100;

        return new ResultResponse(
                total,
                correctCount,
                accuracy,
                score
        );
    }
}