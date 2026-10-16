package com.smarthire.service;

import com.smarthire.dto.request.CreateHiringDriveRequest;
import com.smarthire.dto.request.UpdateHiringDriveStatusRequest;
import com.smarthire.dto.response.HiringDriveResponse;

import java.util.List;
import java.util.UUID;

public interface HiringDriveService {

    HiringDriveResponse createHiringDrive(CreateHiringDriveRequest request);

    List<HiringDriveResponse> getHiringDrivesForCurrentCompany();

    HiringDriveResponse updateStatus(UUID hiringDriveId, UpdateHiringDriveStatusRequest request);
}
