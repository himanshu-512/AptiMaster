package com.aptitudeapp.backend.repository;

import com.aptitudeapp.backend.model.ContestSubmission;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ContestSubmissionRepository extends MongoRepository<ContestSubmission, String> {

    Optional<ContestSubmission> findByContestIdAndUserId(String contestId, String userId);

    List<ContestSubmission> findByContestIdOrderByScoreDescTimeTakenSecondsAscSubmittedAtAsc(String contestId);
}
