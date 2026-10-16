package com.smarthire.controller;

import com.smarthire.dto.request.CreateInterviewRoundRequest;
import com.smarthire.dto.response.InterviewRoundResponse;
import com.smarthire.service.InterviewRoundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hiring-drives/{hiringDriveId}/rounds")
@RequiredArgsConstructor
public class InterviewRoundController {

    private final InterviewRoundService interviewRoundService;

    @PostMapping
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<InterviewRoundResponse> createRound(
            @PathVariable UUID hiringDriveId,
            @Valid @RequestBody CreateInterviewRoundRequest request
    ) {
        return new ResponseEntity<>(
                interviewRoundService.createRound(hiringDriveId, request),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'INTERVIEWER')")
    public ResponseEntity<List<InterviewRoundResponse>> getRounds(
            @PathVariable UUID hiringDriveId
    ) {
        return ResponseEntity.ok(
                interviewRoundService.getRoundsForHiringDrive(hiringDriveId)
        );
    }
}
