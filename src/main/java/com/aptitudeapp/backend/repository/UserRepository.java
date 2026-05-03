package com.aptitudeapp.backend.repository;

import com.aptitudeapp.backend.model.Role;
import com.aptitudeapp.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByPhoneHash(String phoneHash);

    Optional<User> findByEmail(String email);
    long countByGlobalScoreGreaterThan(int score);
    List<User> findByNameContainingIgnoreCase(String name);
    Page<User> findByRole(Role role, Pageable pageable);
}
