package com.aptitudeapp.backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "bookmarks")
@CompoundIndex(name = "user_question_bookmark", def = "{'userId': 1, 'questionId': 1}", unique = true)
public class Bookmark {

    @Id
    private String id;

    private String userId;
    private String questionId;
    private LocalDateTime createdAt = LocalDateTime.now();
}
