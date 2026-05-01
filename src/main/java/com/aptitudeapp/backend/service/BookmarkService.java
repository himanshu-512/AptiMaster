package com.aptitudeapp.backend.service;

import com.aptitudeapp.backend.dto.BookmarkResponse;
import com.aptitudeapp.backend.model.Bookmark;
import com.aptitudeapp.backend.repository.BookmarkRepository;
import com.aptitudeapp.backend.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final QuestionRepository questionRepository;

    public BookmarkResponse toggle(String userId, String questionId) {
        if (questionId == null || questionId.isBlank()) {
            throw new IllegalArgumentException("Question id is required");
        }

        if (!questionRepository.existsById(questionId)) {
            throw new IllegalArgumentException("Invalid question id");
        }

        return bookmarkRepository.findByUserIdAndQuestionId(userId, questionId)
                .map(bookmark -> {
                    bookmarkRepository.delete(bookmark);
                    return new BookmarkResponse(questionId, false);
                })
                .orElseGet(() -> {
                    Bookmark bookmark = new Bookmark();
                    bookmark.setUserId(userId);
                    bookmark.setQuestionId(questionId);
                    bookmarkRepository.save(bookmark);
                    return new BookmarkResponse(questionId, true);
                });
    }

    public BookmarkResponse status(String userId, String questionId) {
        return new BookmarkResponse(
                questionId,
                bookmarkRepository.existsByUserIdAndQuestionId(userId, questionId)
        );
    }
}
