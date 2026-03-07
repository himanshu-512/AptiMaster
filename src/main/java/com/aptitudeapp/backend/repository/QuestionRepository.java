package com.aptitudeapp.backend.repository;

import com.aptitudeapp.backend.model.Question;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionRepository
        extends MongoRepository<Question, String> {
}
