package com.aptitudeapp.backend.service;

import com.aptitudeapp.backend.dto.QuestionResponse;
import com.aptitudeapp.backend.dto.TopicStatsDTO;
import com.aptitudeapp.backend.model.Attempt;
import com.aptitudeapp.backend.model.Question;
import com.aptitudeapp.backend.repository.AttemptRepository;
import com.aptitudeapp.backend.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AttemptRepository attemptRepository;

    // ✅ 1. GET ALL QUESTIONS BY SUBTOPIC (NO FILTER)
    public List<QuestionResponse> getQuestionsBySubtopic(String subtopic) {

        List<Question> questions = questionRepository.findBySubtopic(subtopic);

        return questions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ✅ 2. GET ALL QUESTIONS (RANDOM PRACTICE)
    public List<QuestionResponse> getAllQuestions() {

        List<Question> questions = questionRepository.findAll();

        return questions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ✅ COMMON MAPPER (BEST PRACTICE)
    private QuestionResponse mapToResponse(Question q) {
        return QuestionResponse.builder()
                .id(q.getId())
                .topic(q.getTopic())
                .subtopic(q.getSubtopic()) // ✅ ADD THIS
                .questionText(q.getQuestionText())
                .options(q.getOptions())
                .correctAnswer(q.getCorrectAnswer())
                .explanation(q.getExplanation())
                .build();
    }

    // ✅ 3. STATS (KEEP AS IT IS — already good)
    public List<TopicStatsDTO> getStats(String topic, String userId) {

        List<Question> questions = questionRepository.findByTopic(topic);
        List<Attempt> attempts = attemptRepository.findByUserId(userId);

        Map<String, Question> questionMap = questions.stream()
                .collect(Collectors.toMap(q -> q.getId(), q -> q));

        Map<String, List<Question>> groupedQuestions =
                questions.stream().collect(Collectors.groupingBy(Question::getSubtopic));

        Map<String, List<Attempt>> groupedAttempts = new HashMap<>();

        for (Attempt attempt : attempts) {
            Question q = questionMap.get(attempt.getQuestionId());
            if (q == null) continue;

            String subtopic = q.getSubtopic();

            groupedAttempts
                    .computeIfAbsent(subtopic, k -> new ArrayList<>())
                    .add(attempt);
        }

        List<TopicStatsDTO> result = new ArrayList<>();

        for (String subtopic : groupedQuestions.keySet()) {

            List<Question> qList = groupedQuestions.get(subtopic);
            int total = qList.size();

            List<Attempt> aList =
                    groupedAttempts.getOrDefault(subtopic, new ArrayList<>());

            int solved = aList.size();

            long correct = aList.stream()
                    .filter(Attempt::isCorrect)
                    .count();

            double accuracy = solved == 0 ? 0 : (correct * 100.0) / solved;

            double progress = total == 0 ? 0 : (solved * 100.0) / total;

            String level;
            if (accuracy > 80) level = "STRONG";
            else if (accuracy > 50) level = "INTERMEDIATE";
            else level = "WEAK";

            TopicStatsDTO dto = new TopicStatsDTO();
            dto.setSubtopic(subtopic);
            dto.setTotalQuestions(total);
            dto.setSolvedQuestions(solved);
            dto.setAccuracy(accuracy);
            dto.setProgress(progress);
            dto.setLevel(level);

            result.add(dto);
        }

        return result;
    }
    public List<QuestionResponse> getQuestionsByTopic(String topic, String difficulty, int count) {

        List<Question> questions = questionRepository.findAll()
                .stream()
                .filter(q -> q.getTopic().equalsIgnoreCase(topic))
                .collect(Collectors.toList()); // ✅ FIXED

        // ✅ difficulty filter
        if (difficulty != null) {
            questions = questions.stream()
                    .filter(q -> q.getDifficulty().name().equalsIgnoreCase(difficulty))
                    .collect(Collectors.toList()); // ✅ FIXED
        }

        // ✅ NOW THIS WORKS
        Collections.shuffle(questions);

        // ✅ limit
        if (questions.size() > count) {
            questions = questions.subList(0, count);
        }

        return questions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    public List<QuestionResponse> getRandomQuestions(int count) {
        System.out.println("hjjjj");
        List<Question> questions = questionRepository.findAll();

        Collections.shuffle(questions);

        if (questions.size() > count) {
            questions = questions.subList(0, count);
        }

        return questions.stream()
                .map(this::mapToResponse) // ✅ NOW WORKS
                .collect(Collectors.toList());
    }
}