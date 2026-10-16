package com.smarthire.service.impl;

import com.smarthire.dto.response.RankedCandidateResponse;
import com.smarthire.entity.Company;
import com.smarthire.entity.EvaluationResult;
import com.smarthire.entity.HiringDrive;
import com.smarthire.exception.ResourceNotFoundException;
import com.smarthire.repository.EvaluationResultRepository;
import com.smarthire.repository.HiringDriveRepository;
import com.smarthire.service.RankingService;
import com.smarthire.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {

    private final EvaluationResultRepository evaluationResultRepository;
    private final HiringDriveRepository hiringDriveRepository;
    private final SecurityService securityService;

    @Override
    @Transactional(readOnly = true)
    public List<RankedCandidateResponse> getRankedResults(UUID hiringDriveId, UUID roundId) {
        Company company = securityService.getCurrentUserCompany();

        HiringDrive hiringDrive = hiringDriveRepository.findById(hiringDriveId)
                .orElseThrow(() -> new ResourceNotFoundException("Hiring drive not found"));

        if (!hiringDrive.getCompany().getId().equals(company.getId())) {
            throw new AccessDeniedException("Access denied: hiring drive does not belong to your company");
        }

        List<EvaluationResult> results =
                evaluationResultRepository.findByHiringDriveIdAndInterviewRoundIdOrderByTotalScoreDesc(
                        hiringDriveId, roundId);

        // Apply standard competition ranking (1,2,2,4,...)
        List<RankedCandidateResponse> ranked = new ArrayList<>();
        int rank = 1;
        for (int i = 0; i < results.size(); i++) {
            if (i > 0) {
                Double prev = results.get(i - 1).getTotalScore();
                Double curr = results.get(i).getTotalScore();
                double prevScore = prev != null ? prev : 0.0;
                double currScore = curr != null ? curr : 0.0;
                if (currScore < prevScore) {
                    rank = i + 1;
                }
            }
            EvaluationResult r = results.get(i);
            ranked.add(RankedCandidateResponse.builder()
                    .rank(rank)
                    .candidateId(r.getCandidate().getId())
                    .fullName(r.getCandidate().getFullName())
                    .email(r.getCandidate().getEmail())
                    .roundScore(r.getTotalScore() != null ? r.getTotalScore() : 0.0)
                    .build());
        }
        return ranked;
    }
}
