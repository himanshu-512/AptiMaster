package com.aptitudeapp.backend.controller;

import com.aptitudeapp.backend.dto.AiSuggestionResponse;
import com.aptitudeapp.backend.service.AiSuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiSuggestionService aiSuggestionService;

    @GetMapping("/suggest-topic")
    public AiSuggestionResponse suggestTopic() {
        return aiSuggestionService.getSuggestion();
    }
}