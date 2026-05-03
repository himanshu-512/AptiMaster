package com.aptitudeapp.backend.service;

import com.aptitudeapp.backend.dto.*;
import com.aptitudeapp.backend.model.Contest;
import com.aptitudeapp.backend.model.ContestRegistration;
import com.aptitudeapp.backend.model.ContestSubmission;
import com.aptitudeapp.backend.model.Question;
import com.aptitudeapp.backend.model.User;
import com.aptitudeapp.backend.repository.ContestRepository;
import com.aptitudeapp.backend.repository.ContestRegistrationRepository;
import com.aptitudeapp.backend.repository.ContestSubmissionRepository;
import com.aptitudeapp.backend.repository.QuestionRepository;
import com.aptitudeapp.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContestService {

    private final ContestRepository contestRepository;
    private final ContestSubmissionRepository submissionRepository;
    private final ContestRegistrationRepository registrationRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    public List<ContestSummaryResponse> list(String type, String userId) {
        LocalDateTime now = LocalDateTime.now();
        List<Contest> contests;

        if ("live".equalsIgnoreCase(type)) {
            contests = contestRepository
                    .findByStartTimeLessThanEqualAndEndTimeGreaterThanEqualOrderByStartTimeAsc(now, now);
        } else if ("past".equalsIgnoreCase(type)) {
            contests = contestRepository.findByEndTimeBeforeOrderByStartTimeDesc(now);
        } else {
            contests = contestRepository.findByStartTimeAfterOrderByStartTimeAsc(now);
        }

        return contests.stream().map(contest -> toSummary(contest, userId)).toList();
    }

    public ContestDetailResponse detail(String contestId, String userId) {
        Contest contest = getContest(contestId);
        boolean submitted = submissionRepository.findByContestIdAndUserId(contestId, userId).isPresent();
        boolean registered = registrationRepository.existsByContestIdAndUserId(contestId, userId);
        List<ContestQuestionResponse> questions = isLive(contest)
                ? getContestQuestions(contest).stream().map(this::toContestQuestion).toList()
                : List.of();

        return new ContestDetailResponse(
                contest.getId(),
                contest.getTitle(),
                contest.getDescription(),
                contest.getStartTime(),
                contest.getEndTime(),
                contest.getDurationMinutes(),
                getQuestionCount(contest),
                status(contest),
                System.currentTimeMillis(),
                submitted,
                registered,
                registrationRepository.countByContestId(contestId),
                questions
        );
    }

    public ContestRegistrationResponse register(String contestId, String userId) {
        Contest contest = getContest(contestId);

        if ("PAST".equals(status(contest))) {
            throw new IllegalArgumentException("Registration is closed for this contest");
        }

        if (!registrationRepository.existsByContestIdAndUserId(contestId, userId)) {
            ContestRegistration registration = new ContestRegistration();
            registration.setContestId(contestId);
            registration.setUserId(userId);
            registrationRepository.save(registration);
        }

        return new ContestRegistrationResponse(
                contestId,
                true,
                registrationRepository.countByContestId(contestId),
                "Contest registered successfully"
        );
    }

    public ContestResultResponse submit(String contestId, String userId, ContestSubmitRequest request) {
        Contest contest = getContest(contestId);

        if (!isLive(contest)) {
            throw new IllegalArgumentException("Contest is not live");
        }

        if (submissionRepository.findByContestIdAndUserId(contestId, userId).isPresent()) {
            throw new IllegalArgumentException("Contest already submitted");
        }

        Map<String, Integer> answers = request.getAnswers() == null ? Map.of() : request.getAnswers();
        List<Question> questions = getContestQuestions(contest);
        int correct = 0;

        for (Question question : questions) {
            Integer selected = answers.get(question.getId());
            if (selected != null && selected == question.getCorrectAnswer()) {
                correct++;
            }
        }

        int score = correct * Math.max(contest.getPointsPerQuestion(), 1);

        ContestSubmission submission = new ContestSubmission();
        submission.setContestId(contestId);
        submission.setUserId(userId);
        submission.setAnswers(new HashMap<>(answers));
        submission.setTotalQuestions(questions.size());
        submission.setCorrectAnswers(correct);
        submission.setScore(score);
        submission.setTimeTakenSeconds(Math.max(0, request.getTimeTakenSeconds()));
        submissionRepository.save(submission);

        return new ContestResultResponse(
                contestId,
                questions.size(),
                correct,
                score,
                submission.getTimeTakenSeconds(),
                getRank(contestId, userId)
        );
    }

    public List<ContestRankEntry> leaderboard(String contestId) {
        List<ContestSubmission> submissions =
                submissionRepository.findByContestIdOrderByScoreDescTimeTakenSecondsAscSubmittedAtAsc(contestId);

        Map<String, User> users = userRepository.findAllById(
                submissions.stream().map(ContestSubmission::getUserId).toList()
        ).stream().collect(Collectors.toMap(User::getId, user -> user));

        List<ContestRankEntry> entries = new ArrayList<>();
        int rank = 1;

        for (ContestSubmission submission : submissions) {
            User user = users.get(submission.getUserId());
            entries.add(new ContestRankEntry(
                    rank++,
                    submission.getUserId(),
                    user != null ? user.getName() : "User",
                    user != null ? user.getAvatar() : null,
                    submission.getScore(),
                    submission.getCorrectAnswers(),
                    submission.getTimeTakenSeconds()
            ));
        }

        return entries;
    }

    private int getRank(String contestId, String userId) {
        List<ContestRankEntry> entries = leaderboard(contestId);
        return entries.stream()
                .filter(entry -> entry.getUserId().equals(userId))
                .findFirst()
                .map(ContestRankEntry::getRank)
                .orElse(0);
    }

    private Contest getContest(String contestId) {
        return contestRepository.findById(contestId)
                .orElseThrow(() -> new IllegalArgumentException("Contest not found"));
    }

    private List<Question> getContestQuestions(Contest contest) {
        if (contest.getQuestionIds() != null && !contest.getQuestionIds().isEmpty()) {
            return questionRepository.findAllById(contest.getQuestionIds());
        }

        return questionRepository.findByContestId(contest.getId());
    }

    private int getQuestionCount(Contest contest) {
        if (contest.getTotalQuestions() > 0) return contest.getTotalQuestions();
        if (contest.getQuestionIds() != null && !contest.getQuestionIds().isEmpty()) {
            return contest.getQuestionIds().size();
        }
        return questionRepository.findByContestId(contest.getId()).size();
    }

    private ContestSummaryResponse toSummary(Contest contest, String userId) {
        return new ContestSummaryResponse(
                contest.getId(),
                contest.getTitle(),
                contest.getDescription(),
                contest.getStartTime(),
                contest.getEndTime(),
                contest.getDurationMinutes(),
                getQuestionCount(contest),
                status(contest),
                userId != null && registrationRepository.existsByContestIdAndUserId(contest.getId(), userId),
                registrationRepository.countByContestId(contest.getId())
        );
    }

    private ContestQuestionResponse toContestQuestion(Question question) {
        return new ContestQuestionResponse(
                question.getId(),
                question.getTopic(),
                question.getSubtopic(),
                question.getDifficulty() != null ? question.getDifficulty().name() : null,
                question.getQuestionText(),
                question.getOptions()
        );
    }

    private boolean isLive(Contest contest) {
        return "LIVE".equals(status(contest));
    }

    private String status(Contest contest) {
        LocalDateTime now = LocalDateTime.now();
        if (contest.getStartTime() != null && now.isBefore(contest.getStartTime())) {
            return "UPCOMING";
        }
        if (contest.getEndTime() != null && now.isAfter(contest.getEndTime())) {
            return "PAST";
        }
        return "LIVE";
    }
}
