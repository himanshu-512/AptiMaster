package com.aptitudeapp.backend.controller;

import com.aptitudeapp.backend.dto.BookmarkResponse;
import com.aptitudeapp.backend.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/{questionId}/toggle")
    public BookmarkResponse toggle(
            @PathVariable String questionId,
            Authentication authentication
    ) {
        return bookmarkService.toggle(authentication.getName(), questionId);
    }

    @GetMapping("/{questionId}/status")
    public BookmarkResponse status(
            @PathVariable String questionId,
            Authentication authentication
    ) {
        return bookmarkService.status(authentication.getName(), questionId);
    }
}
