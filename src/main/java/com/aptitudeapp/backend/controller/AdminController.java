package com.aptitudeapp.backend.controller;

import com.aptitudeapp.backend.model.Contest;
import com.aptitudeapp.backend.model.Question;
import com.aptitudeapp.backend.repository.ContestRepository;
import com.aptitudeapp.backend.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final QuestionRepository questionRepository;
    private final ContestRepository contestRepository;

    @GetMapping("/questions")
    public List<Question> getQuestions(
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String contestId
    ) {
        if (contestId != null && !contestId.isBlank()) {
            return questionRepository.findByContestId(contestId);
        }

        if (topic != null && !topic.isBlank()) {
            return questionRepository.findByTopic(topic);
        }

        return questionRepository.findAll();
    }

    @PostMapping("/questions")
    public Question createQuestion(@RequestBody Question question) {
        question.setId(null);
        return questionRepository.save(question);
    }

    @PostMapping("/questions/bulk")
    public List<Question> createQuestions(@RequestBody List<Question> questions) {
        questions.forEach(question -> question.setId(null));
        return questionRepository.saveAll(questions);
    }

    @PutMapping("/questions/{questionId}")
    public Question updateQuestion(
            @PathVariable String questionId,
            @RequestBody Question question
    ) {
        question.setId(questionId);
        return questionRepository.save(question);
    }

    @DeleteMapping("/questions/{questionId}")
    public void deleteQuestion(@PathVariable String questionId) {
        questionRepository.deleteById(questionId);
    }

    @GetMapping("/contests")
    public List<Contest> getContests() {
        return contestRepository.findAll();
    }

    @PostMapping("/contests")
    public Contest createContest(@RequestBody Contest contest) {
        contest.setId(null);
        return contestRepository.save(contest);
    }

    @PutMapping("/contests/{contestId}")
    public Contest updateContest(
            @PathVariable String contestId,
            @RequestBody Contest contest
    ) {
        contest.setId(contestId);
        return contestRepository.save(contest);
    }

    @DeleteMapping("/contests/{contestId}")
    public void deleteContest(@PathVariable String contestId) {
        contestRepository.deleteById(contestId);
    }
}
