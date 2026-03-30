package com.aptitudeapp.backend.controller;

import com.aptitudeapp.backend.dto.QuestionResponse;
import com.aptitudeapp.backend.dto.TopicStatsDTO;
import com.aptitudeapp.backend.model.Question;
import com.aptitudeapp.backend.repository.QuestionRepository;
import com.aptitudeapp.backend.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final QuestionRepository questionRepository;

    // ✅ 1. GET ALL SUBTOPICS
    @GetMapping("/topics")
    public List<String> getSubtopics(@RequestParam String topic) {

        List<Question> questions = questionRepository.findSubtopicsByTopic(topic);

        return questions.stream()
                .map(Question::getSubtopic)
                .distinct()
                .toList();
    }

    // ✅ 2. GET ALL QUESTIONS BY SUBTOPIC (MAIN API)
    @GetMapping("/by-subtopic")
    public List<QuestionResponse> getBySubtopic(@RequestParam String subtopic) {
        return questionService.getQuestionsBySubtopic(subtopic);
    }

    @GetMapping("/all")
    public List<QuestionResponse> getAll() {
        return questionService.getAllQuestions();
    }

    // ✅ 4. TOPIC STATS (ALREADY GOOD)
    @GetMapping("/topic-stats")
    public List<TopicStatsDTO> getStats(
            @RequestParam String topic,
            @RequestParam String userId
    ) {
        return questionService.getStats(topic, userId);
    }
    @GetMapping("/by-topic")
    public List<QuestionResponse> getByTopic(
            @RequestParam String topic,
            @RequestParam(required = false) String difficulty,
            @RequestParam(defaultValue = "10") int count
    ) {
        return questionService.getQuestionsByTopic(topic, difficulty, count);
    }
    @GetMapping("/random")
    public List<QuestionResponse> getRandomQuestions(
            @RequestParam(defaultValue = "10") int count
    ) {
        return questionService.getRandomQuestions(count);
    }
}