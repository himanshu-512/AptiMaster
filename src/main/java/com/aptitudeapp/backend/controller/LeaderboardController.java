package com.aptitudeapp.backend.controller;

import com.aptitudeapp.backend.dto.LeaderboardEntry;
import com.aptitudeapp.backend.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private static final Logger logger = LoggerFactory.getLogger(LeaderboardController.class);

    private final LeaderboardService leaderboardService;

    @GetMapping("/global")
    public List<LeaderboardEntry> globalLeaderboard(
            @RequestParam int page,
            @RequestParam int limit
    ){
        logger.info("🔥 Global API Hit | page={} limit={}", page, limit);
        return leaderboardService.getGlobalLeaderboard(page, limit);
    }

    @GetMapping("/daily")
    public List<LeaderboardEntry> dailyLeaderboard(
            @RequestParam int page,
            @RequestParam int limit
    ){
        logger.info("🔥 Daily API Hit | page={} limit={}", page, limit);
        return leaderboardService.getDailyLeaderboard(page, limit);
    }

    @GetMapping("/weekly")
    public List<LeaderboardEntry> weeklyLeaderboard(
            @RequestParam int page,
            @RequestParam int limit
    ){
        logger.info("🔥 Weekly API Hit | page={} limit={}", page, limit);
        return leaderboardService.getWeeklyLeaderboard(page, limit);
    }

    @GetMapping("/search")
    public List<LeaderboardEntry> searchUser(
            @RequestParam String query
    ){
        logger.info("🔎 Search API Hit | query={}", query);
        return leaderboardService.searchUser(query);
    }

    @GetMapping("/rank/{userId}")
    public long getUserRank(@PathVariable String userId) {
        logger.info("🏆 Get Rank API Hit | userId={}", userId);
        return leaderboardService.getUserRank(userId);
    }
}