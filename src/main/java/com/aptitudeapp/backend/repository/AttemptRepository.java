package com.aptitudeapp.backend.repository;

import com.aptitudeapp.backend.model.Attempt;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AttemptRepository extends MongoRepository<Attempt, String> {
    List<Attempt> findByUserId(String userId);

    List<Attempt> findByUserIdAndAttemptedAtAfter(
            String userId,
            LocalDateTime date
    );
//    List<Attempt> findByUserId(String userId);
}
