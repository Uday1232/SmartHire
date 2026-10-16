package com.smarthire.service;

import com.smarthire.dto.request.CreateInterviewRoundRequest;
import com.smarthire.dto.response.InterviewRoundResponse;

import java.util.List;
import java.util.UUID;

public interface InterviewRoundService {

    InterviewRoundResponse createRound(UUID hiringDriveId, CreateInterviewRoundRequest request);

    List<InterviewRoundResponse> getRoundsForHiringDrive(UUID hiringDriveId);
}
