package com.aptitudeapp.backend.service;

import com.aptitudeapp.backend.dto.QuestionResponse;
import com.aptitudeapp.backend.dto.PracticeRequest;
import com.aptitudeapp.backend.dto.TopicStatsDTO;
import com.aptitudeapp.backend.model.Attempt;
import com.aptitudeapp.backend.model.Bookmark;
import com.aptitudeapp.backend.model.Difficulty;
import com.aptitudeapp.backend.model.Question;
import com.aptitudeapp.backend.repository.AttemptRepository;
import com.aptitudeapp.backend.repository.BookmarkRepository;
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
    private final BookmarkRepository bookmarkRepository;

    // ✅ 1. GET ALL QUESTIONS BY SUBTOPIC (NO FILTER)
    public List<QuestionResponse> getQuestionsBySubtopic(String subtopic) {

        if (subtopic == null || subtopic.isBlank() || "undefined".equalsIgnoreCase(subtopic)) {
            return List.of();
        }

        List<Question> questions = questionRepository.findBySubtopicIgnoreCase(subtopic.trim());

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
                .subtopic(q.getSubtopic())
                .difficulty(q.getDifficulty() != null ? q.getDifficulty().name() : null)
                .questionText(q.getQuestionText())
                .options(q.getOptions())
                .correctAnswer(q.getCorrectAnswer())
                .explanation(q.getExplanation())
                .build();
    }

    // ✅ 3. STATS (KEEP AS IT IS — already good)
    public List<TopicStatsDTO> getStats(String topic, String userId) {

        List<Question> questions = findQuestionsByTopicFlexible(topic);
        List<Attempt> attempts = attemptRepository.findByUserId(userId);

        Map<String, Question> questionMap = questions.stream()
                .collect(Collectors.toMap(q -> q.getId(), q -> q));

        Map<String, List<Question>> groupedQuestions =
                questions.stream().collect(Collectors.groupingBy(q ->
                        q.getSubtopic() != null ? q.getSubtopic() : "General"
                ));

        Map<String, List<Attempt>> groupedAttempts = new HashMap<>();

        for (Attempt attempt : attempts) {
            Question q = questionMap.get(attempt.getQuestionId());
            if (q == null) continue;

            String subtopic = q.getSubtopic() != null ? q.getSubtopic() : "General";

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

        if (topic == null || topic.isBlank()) {
            throw new IllegalArgumentException("Topic is required");
        }

        int safeCount = Math.max(1, Math.min(count, 50));
        List<Question> questions;

        if (difficulty != null && !difficulty.isBlank()) {
            Difficulty parsedDifficulty = Difficulty.valueOf(difficulty.toUpperCase());
            questions = questionRepository.findByTopicInAndDifficulty(topicAliases(topic), parsedDifficulty);
            if (questions.isEmpty()) {
                questions = findQuestionsByTopicFlexible(topic).stream()
                        .filter(q -> q.getDifficulty() == parsedDifficulty)
                        .collect(Collectors.toList());
            }
        } else {
            questions = findQuestionsByTopicFlexible(topic);
        }

        Collections.shuffle(questions);

        if (questions.size() > safeCount) {
            questions = questions.subList(0, safeCount);
        }

        return questions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<QuestionResponse> getPracticeQuestions(PracticeRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Practice request is required");
        }

        int safeCount = Math.max(1, Math.min(request.getCount() > 0 ? request.getCount() : 10, 50));
        List<Question> questions;

        if (request.getSubtopic() != null && !request.getSubtopic().isBlank()) {
            questions = questionRepository.findBySubtopicIgnoreCase(request.getSubtopic().trim());
        } else if (request.getTopic() != null && !request.getTopic().isBlank()) {
            questions = findQuestionsByTopicFlexible(request.getTopic());
        } else {
            questions = questionRepository.findAll();
        }

        if (request.getDifficulty() != null && !request.getDifficulty().isBlank()) {
            Difficulty parsedDifficulty = Difficulty.valueOf(request.getDifficulty().toUpperCase());
            questions = questions.stream()
                    .filter(q -> q.getDifficulty() == parsedDifficulty)
                    .collect(Collectors.toList());
        }

        Collections.shuffle(questions);

        if (questions.size() > safeCount) {
            questions = questions.subList(0, safeCount);
        }

        return questions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<String> getSubtopics(String topic) {
        List<Question> questions = questionRepository.findSubtopicsByTopicIn(topicAliases(topic));
        if (questions.isEmpty()) {
            questions = findQuestionsByTopicFlexible(topic);
        }

        return questions.stream()
                .map(Question::getSubtopic)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    public List<QuestionResponse> getRandomQuestions(int count) {
        int safeCount = Math.max(1, Math.min(count, 50));
        List<Question> questions = questionRepository.findAll();

        Collections.shuffle(questions);

        if (questions.size() > safeCount) {
            questions = questions.subList(0, safeCount);
        }

        return questions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<QuestionResponse> getWrongQuestions(String userId, int count) {
        int safeCount = Math.max(1, Math.min(count, 50));

        List<String> questionIds = attemptRepository.findByUserIdAndCorrectFalse(userId)
                .stream()
                .sorted(Comparator.comparing(Attempt::getAttemptedAt).reversed())
                .map(Attempt::getQuestionId)
                .distinct()
                .limit(safeCount)
                .toList();

        return questionRepository.findAllById(questionIds)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<QuestionResponse> getBookmarkedQuestions(String userId, int count) {
        int safeCount = Math.max(1, Math.min(count, 50));

        List<String> questionIds = bookmarkRepository.findByUserId(userId)
                .stream()
                .sorted(Comparator.comparing(Bookmark::getCreatedAt).reversed())
                .map(Bookmark::getQuestionId)
                .limit(safeCount)
                .toList();

        return questionRepository.findAllById(questionIds)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<QuestionResponse> getSmartPracticeQuestions(String userId, int count) {
        int safeCount = Math.max(1, Math.min(count, 50));
        List<Attempt> attempts = attemptRepository.findByUserId(userId);

        String weakTopic = attempts.stream()
                .collect(Collectors.groupingBy(
                        Attempt::getTopic,
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            long correct = list.stream().filter(Attempt::isCorrect).count();
                            return list.isEmpty() ? 100.0 : (correct * 100.0) / list.size();
                        })
                ))
                .entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        if (weakTopic == null || weakTopic.isBlank()) {
            return getRandomQuestions(safeCount);
        }

        List<Question> questions = findQuestionsByTopicFlexible(weakTopic);
        Collections.shuffle(questions);

        if (questions.size() > safeCount) {
            questions = questions.subList(0, safeCount);
        }

        return questions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private List<String> topicAliases(String topic) {
        String value = topic == null ? "" : topic.trim();
        String lower = value.toLowerCase();
        LinkedHashSet<String> aliases = new LinkedHashSet<>();

        if (!value.isBlank()) {
            aliases.add(value);
        }

        if (lower.contains("quant")) {
            aliases.add("Quant");
            aliases.add("Quantitative");
            aliases.add("Quantitative Aptitude");
        } else if (lower.contains("logical") || lower.contains("reasoning")) {
            aliases.add("Logical");
            aliases.add("Reasoning");
            aliases.add("Logical Reasoning");
            aliases.add("Logical Aptitude");
        } else if (lower.contains("verbal")) {
            aliases.add("Verbal");
            aliases.add("Verbal Ability");
            aliases.add("Verbal Reasoning");
        } else if (lower.contains("data") || lower.equals("di")) {
            aliases.add("DI");
            aliases.add("Data Interpretation");
        }

        return new ArrayList<>(aliases);
    }

    private List<Question> findQuestionsByTopicFlexible(String topic) {
        List<String> aliases = topicAliases(topic);
        List<Question> questions = questionRepository.findByTopicIn(aliases);

        if (!questions.isEmpty()) {
            return questions;
        }

        return questionRepository.findAll().stream()
                .filter(q -> aliases.stream().anyMatch(alias -> sameText(alias, q.getTopic())))
                .collect(Collectors.toList());
    }

    private boolean sameText(String left, String right) {
        return left != null && right != null && left.trim().equalsIgnoreCase(right.trim());
    }
}

