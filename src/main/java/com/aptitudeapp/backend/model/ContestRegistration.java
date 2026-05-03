package com.aptitudeapp.backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "contest_registrations")
@CompoundIndex(name = "contest_user_unique", def = "{'contestId':1,'userId':1}", unique = true)
public class ContestRegistration {

    @Id
    private String id;

    private String contestId;
    private String userId;
    private LocalDateTime registeredAt = LocalDateTime.now();
}
