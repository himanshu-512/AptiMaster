package com.aptitudeapp.backend.controller;

import com.aptitudeapp.backend.dto.QuestionResponse;
import com.aptitudeapp.backend.dto.PracticeRequest;
import com.aptitudeapp.backend.dto.TopicStatsDTO;
import com.aptitudeapp.backend.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    // ✅ 1. GET ALL SUBTOPICS
    @GetMapping("/topics")
    public List<String> getSubtopics(@RequestParam String topic) {

        return questionService.getSubtopics(topic);
    }

    // ✅ 2. GET ALL QUESTIONS BY SUBTOPIC (MAIN API)
    @GetMapping("/by-subtopic")
    public List<QuestionResponse> getBySubtopic(@RequestParam String subtopic) {
        return questionService.getQuestionsBySubtopic(subtopic);
    }

    @PostMapping("/by-subtopic")
    public List<QuestionResponse> getPracticeQuestions(@RequestBody PracticeRequest request) {
        return questionService.getPracticeQuestions(request);
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

    @GetMapping("/wrong")
    public List<QuestionResponse> getWrongQuestions(
            @RequestParam(defaultValue = "10") int count,
            Authentication authentication
    ) {
        return questionService.getWrongQuestions(authentication.getName(), count);
    }

    @GetMapping("/bookmarked")
    public List<QuestionResponse> getBookmarkedQuestions(
            @RequestParam(defaultValue = "10") int count,
            Authentication authentication
    ) {
        return questionService.getBookmarkedQuestions(authentication.getName(), count);
    }

    @GetMapping("/smart-practice")
    public List<QuestionResponse> getSmartPracticeQuestions(
            @RequestParam(defaultValue = "10") int count,
            Authentication authentication
    ) {
        return questionService.getSmartPracticeQuestions(authentication.getName(), count);
    }
}
