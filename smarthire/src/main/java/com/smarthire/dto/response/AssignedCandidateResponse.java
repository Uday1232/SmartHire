package com.smarthire.dto.response;

import com.smarthire.enums.SessionStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class AssignedCandidateResponse {

    private UUID candidateId;
    private String fullName;
    private String email;
    private UUID hiringDriveId;
    private String hiringDriveName;
    private UUID sessionId;         // nullable — null if no session exists yet
    private SessionStatus sessionStatus; // nullable — null if no session exists yet
}
