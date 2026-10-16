package com.smarthire.dto.response;

import com.smarthire.enums.HiringDriveStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
public class HiringDriveResponse {

    private UUID id;

    private UUID companyId;

    private String companyName;

    private String title;

    private String description;

    private HiringDriveStatus status;

    private LocalDate startDate;

    private LocalDate endDate;
}
