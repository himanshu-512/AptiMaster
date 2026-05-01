package com.aptitudeapp.backend.repository;

import com.aptitudeapp.backend.model.Question;
import com.aptitudeapp.backend.model.Difficulty;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Collection;

public interface QuestionRepository
        extends MongoRepository<Question, String> {
    List<Question> findBySubtopic(String subtopic);
    List<Question> findBySubtopicIgnoreCase(String subtopic);
    @Query(value = "{ 'topic': ?0 }", fields = "{ 'subtopic': 1 }")
    List<Question> findSubtopicsByTopic(String topic);
    @Query(value = "{ 'topic': { $in: ?0 } }", fields = "{ 'subtopic': 1 }")
    List<Question> findSubtopicsByTopicIn(Collection<String> topics);
    List<Question> findByTopic(String topic);
    List<Question> findByTopicIn(Collection<String> topics);
    List<Question> findByTopicAndDifficulty(String topic, Difficulty difficulty);
    List<Question> findByTopicInAndDifficulty(Collection<String> topics, Difficulty difficulty);
    List<Question> findByContestId(String contestId);
}
