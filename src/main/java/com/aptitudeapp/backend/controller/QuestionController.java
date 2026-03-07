package com.aptitudeapp.backend.controller;


import com.aptitudeapp.backend.dto.PracticeRequest;
import com.aptitudeapp.backend.dto.QuestionResponse;
import com.aptitudeapp.backend.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/practice")
    public List<QuestionResponse> getPracticeSet(
            @RequestBody PracticeRequest request
    ) {
        return questionService.getPracticeSet(
                request.getTopic(),
                request.getDifficulty(),
                request.getCount()
        );
    }
}