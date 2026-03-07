package com.aptitudeapp.backend.controller;

import com.aptitudeapp.backend.dto.LeaderboardEntry;
import com.aptitudeapp.backend.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @GetMapping("/global")
    public List<LeaderboardEntry> globalLeaderboard(
            @RequestParam int page,
            @RequestParam int limit
    ){
        return leaderboardService.getGlobalLeaderboard(page, limit);
    }

    @GetMapping("/daily")
    public List<LeaderboardEntry> dailyLeaderboard(
            @RequestParam int page,
            @RequestParam int limit
    ){
        return leaderboardService.getDailyLeaderboard(page, limit);
    }

    @GetMapping("/weekly")
    public List<LeaderboardEntry> weeklyLeaderboard(
            @RequestParam int page,
            @RequestParam int limit
    ){
        return leaderboardService.getWeeklyLeaderboard(page, limit);
    }
    @GetMapping("/search")
    public List<LeaderboardEntry> searchUser(
            @RequestParam String query
    ){
        return leaderboardService.searchUser(query);
    }
}