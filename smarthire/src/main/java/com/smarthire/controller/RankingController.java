package com.smarthire.controller;

import com.smarthire.dto.response.RankedCandidateResponse;
import com.smarthire.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hiring-drives/{hiringDriveId}/rounds/{roundId}/rankings")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    @GetMapping
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<List<RankedCandidateResponse>> getRankedResults(
            @PathVariable UUID hiringDriveId,
            @PathVariable UUID roundId
    ) {
        return ResponseEntity.ok(
                rankingService.getRankedResults(hiringDriveId, roundId)
        );
    }
}
