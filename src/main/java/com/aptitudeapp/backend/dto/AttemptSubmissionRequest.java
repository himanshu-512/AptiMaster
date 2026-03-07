package com.aptitudeapp.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AttemptSubmissionRequest {

    private List<AttemptRequest> attempts;
}
