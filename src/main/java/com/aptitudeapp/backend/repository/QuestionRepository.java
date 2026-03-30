package com.aptitudeapp.backend.repository;

import com.aptitudeapp.backend.model.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface QuestionRepository
        extends MongoRepository<Question, String> {
    List<Question> findBySubtopic(String subtopic);
    @Query(value = "{ 'topic': ?0 }", fields = "{ 'subtopic': 1 }")
    List<Question> findSubtopicsByTopic(String topic);
    List<Question> findByTopic(String topic);
    List<Question> findByTopicAndSubtopicAndDifficulty(
            String topic,
            String subtopic,
            String difficulty
    );
}
