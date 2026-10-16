package com.smarthire.service;

import com.smarthire.dto.request.CreateCandidateRequest;
import com.smarthire.dto.response.CandidateResponse;
import com.smarthire.dto.response.ExcelUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface CandidateService {

    CandidateResponse createCandidate(UUID hiringDriveId, CreateCandidateRequest request);

    List<CandidateResponse> getCandidatesForHiringDrive(UUID hiringDriveId);

    ExcelUploadResponse uploadCandidatesFromExcel(UUID hiringDriveId, MultipartFile file);

    ExcelUploadResponse importCandidatesFromUrl(UUID hiringDriveId, String url);
}
