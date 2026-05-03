package com.aptitudeapp.backend.repository;

import com.aptitudeapp.backend.model.ContestRegistration;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ContestRegistrationRepository extends MongoRepository<ContestRegistration, String> {

    boolean existsByContestIdAndUserId(String contestId, String userId);

    long countByContestId(String contestId);

    Optional<ContestRegistration> findByContestIdAndUserId(String contestId, String userId);
}
