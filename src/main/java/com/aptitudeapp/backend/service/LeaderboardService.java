package com.aptitudeapp.backend.service;

import com.aptitudeapp.backend.dto.LeaderboardEntry;
import com.aptitudeapp.backend.model.User;
import com.aptitudeapp.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final UserRepository userRepository;

    // GLOBAL LEADERBOARD
    public List<LeaderboardEntry> getGlobalLeaderboard(int page, int limit) {

        return getLeaderboard(page, limit, "globalScore");
    }

    // DAILY LEADERBOARD
    public List<LeaderboardEntry> getDailyLeaderboard(int page, int limit) {

        return getLeaderboard(page, limit, "dailyScore");
    }

    // WEEKLY LEADERBOARD
    public List<LeaderboardEntry> getWeeklyLeaderboard(int page, int limit) {

        return getLeaderboard(page, limit, "weeklyScore");
    }

    // COMMON METHOD
    private List<LeaderboardEntry> getLeaderboard(
            int page,
            int limit,
            String scoreField
    ) {

        PageRequest pageable = PageRequest.of(
                page,
                limit,
                Sort.by(Sort.Direction.DESC, scoreField)
        );

        List<User> users =
                userRepository.findAll(pageable).getContent();

        List<LeaderboardEntry> leaderboard =
                new ArrayList<>();

        int rank = page * limit + 1;

        for (User user : users) {

            int points = 0;

            if (scoreField.equals("globalScore"))
                points = user.getGlobalScore();

            if (scoreField.equals("dailyScore"))
                points = user.getDailyScore();

            if (scoreField.equals("weeklyScore"))
                points = user.getWeeklyScore();

            leaderboard.add(
                    new LeaderboardEntry(
                            rank++,
                            user.getId(),
                            user.getName(),
                            user.getAvatar(),
                            points
                    )
            );
        }

        return leaderboard;
    }

    // USER RANK (GLOBAL)
    public long getUserRank(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow();

        long higherScores = userRepository
                .countByGlobalScoreGreaterThan(user.getGlobalScore());

        return higherScores + 1;
    }
    public List<LeaderboardEntry> searchUser(String query) {

        List<User> users = userRepository
                .findByNameContainingIgnoreCase(query);

        List<LeaderboardEntry> result = new ArrayList<>();

        for (User user : users) {

            long higherScores = userRepository
                    .countByGlobalScoreGreaterThan(user.getGlobalScore());

            int rank = (int) higherScores + 1;

            result.add(
                    new LeaderboardEntry(
                            rank,
                            user.getId(),
                            user.getName(),
                            user.getAvatar(),
                            user.getGlobalScore()
                    )
            );
        }

        return result;
    }
}