package com.aptitudeapp.backend.repository;

import com.aptitudeapp.backend.model.Contest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ContestRepository extends MongoRepository<Contest, String> {

    List<Contest> findByEndTimeBeforeOrderByStartTimeDesc(LocalDateTime now);

    List<Contest> findByStartTimeAfterOrderByStartTimeAsc(LocalDateTime now);

    List<Contest> findByStartTimeLessThanEqualAndEndTimeGreaterThanEqualOrderByStartTimeAsc(
            LocalDateTime start,
            LocalDateTime end
    );
}
