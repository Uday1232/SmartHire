package com.smarthire.controller;

import com.smarthire.dto.request.CreateHiringDriveRequest;
import com.smarthire.dto.request.UpdateHiringDriveStatusRequest;
import com.smarthire.dto.response.HiringDriveResponse;
import com.smarthire.service.HiringDriveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hiring-drives")
@RequiredArgsConstructor
public class HiringDriveController {

    private final HiringDriveService hiringDriveService;

    @PostMapping
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<HiringDriveResponse> createHiringDrive(
            @Valid @RequestBody CreateHiringDriveRequest request
    ) {

        return new ResponseEntity<>(
                hiringDriveService.createHiringDrive(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'INTERVIEWER')")
    public ResponseEntity<List<HiringDriveResponse>> getHiringDrives() {

        return ResponseEntity.ok(
                hiringDriveService.getHiringDrivesForCurrentCompany()
        );
    }

    @PatchMapping("/{hiringDriveId}/status")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<HiringDriveResponse> updateStatus(
            @PathVariable UUID hiringDriveId,
            @Valid @RequestBody UpdateHiringDriveStatusRequest request
    ) {
        return ResponseEntity.ok(
                hiringDriveService.updateStatus(hiringDriveId, request)
        );
    }
}
