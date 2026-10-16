package com.smarthire.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CandidateProjectResponse {

    private UUID id;
    private String projectName;
    private String techStack;
    private String description;
}
