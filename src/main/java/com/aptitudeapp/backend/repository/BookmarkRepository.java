package com.aptitudeapp.backend.repository;

import com.aptitudeapp.backend.model.Bookmark;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends MongoRepository<Bookmark, String> {

    List<Bookmark> findByUserId(String userId);

    Optional<Bookmark> findByUserIdAndQuestionId(String userId, String questionId);

    boolean existsByUserIdAndQuestionId(String userId, String questionId);

    long countByUserId(String userId);
}
