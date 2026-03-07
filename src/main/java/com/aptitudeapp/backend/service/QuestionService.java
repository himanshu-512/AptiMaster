package com.aptitudeapp.backend.service;

import com.aptitudeapp.backend.dto.QuestionResponse;
import com.aptitudeapp.backend.model.Difficulty;
import com.aptitudeapp.backend.model.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final MongoTemplate mongoTemplate;

    public List<QuestionResponse> getPracticeSet(
            String topic,
            String difficulty,
            int count
    ) {

        Difficulty diff = Difficulty.valueOf(difficulty.toUpperCase());

        Aggregation aggregation = Aggregation.newAggregation(

                Aggregation.match(
                        Criteria.where("topic")
                                .regex(topic, "i")   // case insensitive topic match
                                .and("difficulty").is(diff)
                ),

                Aggregation.sample(count)
        );

        List<Question> questions =
                mongoTemplate.aggregate(
                        aggregation,
                        "questions",
                        Question.class
                ).getMappedResults();

        return questions.stream()
                .map(q -> QuestionResponse.builder()
                        .id(q.getId())
                        .topic(q.getTopic())
                        .difficulty(q.getDifficulty().name())
                        .questionText(q.getQuestionText())
                        .options(q.getOptions())
                        .correctAnswer(q.getCorrectAnswer())   // ADD THIS
                        .explanation(q.getExplanation())       // ADD THIS
                        .build())
                .collect(Collectors.toList());
    }
}