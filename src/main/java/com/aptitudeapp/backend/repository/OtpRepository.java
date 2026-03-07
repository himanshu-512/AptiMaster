package com.aptitudeapp.backend.repository;

import com.aptitudeapp.backend.model.Otp;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OtpRepository extends MongoRepository<Otp, String> {

    Optional<Otp> findByPhoneHash(String phoneHash);

    void deleteByPhoneHash(String phoneHash);
}
