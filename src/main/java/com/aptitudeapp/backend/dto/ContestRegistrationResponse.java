package com.aptitudeapp.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContestRegistrationResponse {

    private String contestId;
    private boolean registered;
    private long registeredUsers;
    private String message;
}
