package com.aptitudeapp.backend.service;

import com.aptitudeapp.backend.dto.LeaderboardEntry;
import com.aptitudeapp.backend.model.Role;
import com.aptitudeapp.backend.model.User;
import com.aptitudeapp.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private static final Logger logger = LoggerFactory.getLogger(LeaderboardService.class);

    private final UserRepository userRepository;

    // GLOBAL
    public List<LeaderboardEntry> getGlobalLeaderboard(int page, int limit) {
        logger.info("➡️ Global leaderboard called");
        return getLeaderboard(page, limit, "globalScore");
    }

    // DAILY
    public List<LeaderboardEntry> getDailyLeaderboard(int page, int limit) {
        logger.info("➡️ Daily leaderboard called");
        return getLeaderboard(page, limit, "dailyScore");
    }

    // WEEKLY
    public List<LeaderboardEntry> getWeeklyLeaderboard(int page, int limit) {
        logger.info("➡️ Weekly leaderboard called");
        return getLeaderboard(page, limit, "weeklyScore");
    }

    // COMMON METHOD
    private List<LeaderboardEntry> getLeaderboard(
            int page,
            int limit,
            String scoreField
    ) {

        logger.info("📊 Fetching leaderboard | page={} limit={} field={}", page, limit, scoreField);

        PageRequest pageable = PageRequest.of(
                page,
                limit,
                Sort.by(Sort.Direction.DESC, scoreField)
                        .and(Sort.by(Sort.Direction.ASC, "createdAt"))
        );

        // 🔥 ONLY NORMAL USERS
        List<User> users = userRepository
                .findByRole(Role.USER, pageable)
                .getContent();

        logger.info("✅ Users fetched (USER only): {}", users.size());

        List<LeaderboardEntry> leaderboard = new ArrayList<>();

        int rank = page * limit + 1;

        for (User user : users) {

            int points = switch (scoreField) {
                case "globalScore" -> user.getGlobalScore();
                case "dailyScore" -> user.getDailyScore();
                case "weeklyScore" -> user.getWeeklyScore();
                default -> 0;
            };

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

    // USER RANK
    public long getUserRank(String userId) {

        logger.info("🔍 Getting rank for userId={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        long higherScores = userRepository
                .countByGlobalScoreGreaterThan(user.getGlobalScore());

        long rank = higherScores + 1;

        logger.info("🏆 Rank = {}", rank);

        return rank;
    }

    // SEARCH
    public List<LeaderboardEntry> searchUser(String query) {

        logger.info("🔎 Searching users: {}", query);

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

        logger.info("✅ Search result count: {}", result.size());

        return result;
    }
}