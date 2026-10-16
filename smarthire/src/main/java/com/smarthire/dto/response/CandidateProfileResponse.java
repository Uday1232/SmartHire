package com.smarthire.dto.response;

import com.smarthire.enums.ProfileStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class CandidateProfileResponse {

    private UUID id;
    private UUID candidateId;
    private String candidateFullName;
    private List<String> primarySkills;
    private List<String> secondarySkills;
    private List<CandidateProjectResponse> projects;
    private ProfileStatus status;
    private LocalDateTime parsedAt;
}
