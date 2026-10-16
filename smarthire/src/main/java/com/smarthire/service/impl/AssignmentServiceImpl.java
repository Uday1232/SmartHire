package com.smarthire.service.impl;

import com.smarthire.dto.request.AssignCandidateRequest;
import com.smarthire.dto.response.AssignedCandidateResponse;
import com.smarthire.dto.response.AssignmentResponse;
import com.smarthire.entity.InterviewerAssignment;
import com.smarthire.entity.InterviewRound;
import com.smarthire.entity.InterviewSession;
import com.smarthire.mapper.AssignmentMapper;
import com.smarthire.repository.InterviewerAssignmentRepository;
import com.smarthire.repository.InterviewRoundRepository;
import com.smarthire.repository.InterviewSessionRepository;
import com.smarthire.service.AssignmentService;
import com.smarthire.entity.Candidate;
import com.smarthire.entity.Company;
import com.smarthire.entity.HiringDrive;
import com.smarthire.entity.User;
import com.smarthire.enums.CandidateStatus;
import com.smarthire.enums.HiringDriveStatus;
import com.smarthire.enums.Role;
import com.smarthire.enums.SessionStatus;
import com.smarthire.exception.ResourceAlreadyExistsException;
import com.smarthire.exception.ResourceNotFoundException;
import com.smarthire.repository.CandidateRepository;
import com.smarthire.repository.HiringDriveRepository;
import com.smarthire.repository.UserRepository;
import com.smarthire.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {

    private final InterviewerAssignmentRepository assignmentRepository;
    private final HiringDriveRepository hiringDriveRepository;
    private final CandidateRepository candidateRepository;
    private final UserRepository userRepository;
    private final InterviewRoundRepository roundRepository;
    private final InterviewSessionRepository sessionRepository;
    private final SecurityService securityService;
    private final AssignmentMapper assignmentMapper;

    @Override
    @Transactional
    public List<AssignmentResponse> assignInterviewers(
            UUID hiringDriveId,
            UUID candidateId,
            AssignCandidateRequest request
    ) {
        Company company = securityService.getCurrentUserCompany();
        HiringDrive hiringDrive = resolveCompanyHiringDrive(hiringDriveId, company.getId());
        validateHiringDriveIsActive(hiringDrive);

        Candidate candidate = resolveHiringDriveCandidate(candidateId, hiringDriveId);

        // Validate the round belongs to this hiring drive
        InterviewRound round = roundRepository.findById(request.getInterviewRoundId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Interview round not found: " + request.getInterviewRoundId()));
        if (!round.getHiringDrive().getId().equals(hiringDriveId)) {
            throw new ResourceNotFoundException("Interview round does not belong to this hiring drive");
        }

        List<InterviewerAssignment> created = new ArrayList<>();

        for (UUID interviewerId : request.getInterviewerIds()) {
            User interviewer = userRepository.findById(interviewerId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Interviewer not found with id: " + interviewerId));

            validateInterviewerRole(interviewer, interviewerId);
            validateInterviewerBelongsToCompany(interviewer, company.getId(), interviewerId);

            if (assignmentRepository.existsByHiringDriveIdAndInterviewerIdAndCandidateId(
                    hiringDriveId, interviewerId, candidateId)) {
                throw new ResourceAlreadyExistsException(
                        "Interviewer " + interviewerId + " is already assigned to this candidate");
            }

            InterviewerAssignment assignment = new InterviewerAssignment();
            assignment.setHiringDrive(hiringDrive);
            assignment.setInterviewer(interviewer);
            assignment.setCandidate(candidate);

            created.add(assignmentRepository.save(assignment));

            // Create a PENDING InterviewSession for this interviewer+candidate+round
            // (skip if one already exists due to idempotency)
            boolean sessionExists = sessionRepository
                    .findByInterviewerIdAndCandidateIdAndInterviewRoundId(
                            interviewerId, candidateId, round.getId())
                    .isPresent();
            if (!sessionExists) {
                InterviewSession session = new InterviewSession();
                session.setInterviewer(interviewer);
                session.setCandidate(candidate);
                session.setInterviewRound(round);
                session.setStatus(SessionStatus.PENDING);
                sessionRepository.save(session);
            }
        }

        // Promote candidate status only if still IMPORTED
        if (candidate.getStatus() == CandidateStatus.IMPORTED) {
            candidate.setStatus(CandidateStatus.ASSIGNED);
            candidateRepository.save(candidate);
        }

        return created.stream()
                .map(assignmentMapper::toAssignmentResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssignmentResponse> getAssignmentsForHiringDrive(UUID hiringDriveId) {
        Company company = securityService.getCurrentUserCompany();
        resolveCompanyHiringDrive(hiringDriveId, company.getId());

        return assignmentRepository.findByHiringDriveIdOrderByCreatedAtDesc(hiringDriveId)
                .stream()
                .map(assignmentMapper::toAssignmentResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssignedCandidateResponse> getMyAssignedCandidates() {
        User currentUser = securityService.getCurrentUser();
        Company company = securityService.getCurrentUserCompany();

        return assignmentRepository
                .findByInterviewerIdAndHiringDrive_Company_IdOrderByCreatedAtDesc(
                        currentUser.getId(),
                        company.getId()
                )
                .stream()
                .map(assignmentMapper::toAssignedCandidateResponse)
                .toList();
    }

    @Override
    @Transactional
    public void removeAssignment(UUID hiringDriveId, UUID candidateId, UUID assignmentId) {
        Company company = securityService.getCurrentUserCompany();
        resolveCompanyHiringDrive(hiringDriveId, company.getId());

        InterviewerAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

        // Verify this assignment belongs to the given hiring drive and candidate
        if (!assignment.getHiringDrive().getId().equals(hiringDriveId) ||
                !assignment.getCandidate().getId().equals(candidateId)) {
            throw new ResourceNotFoundException("Assignment not found");
        }

        assignmentRepository.delete(assignment);
    }

    // ── Private helpers ──────────────────────────────────────────────────────

    private HiringDrive resolveCompanyHiringDrive(UUID hiringDriveId, UUID companyId) {
        HiringDrive hiringDrive = hiringDriveRepository.findById(hiringDriveId)
                .orElseThrow(() -> new ResourceNotFoundException("Hiring drive not found"));

        if (!hiringDrive.getCompany().getId().equals(companyId)) {
            // Intentionally return 404 to avoid information leakage
            throw new ResourceNotFoundException("Hiring drive not found");
        }

        return hiringDrive;
    }

    private void validateHiringDriveIsActive(HiringDrive hiringDrive) {
        if (hiringDrive.getStatus() != HiringDriveStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Assignments can only be created for ACTIVE hiring drives. " +
                            "Current status: " + hiringDrive.getStatus());
        }
    }

    private Candidate resolveHiringDriveCandidate(UUID candidateId, UUID hiringDriveId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

        if (!candidate.getHiringDrive().getId().equals(hiringDriveId)) {
            throw new ResourceNotFoundException("Candidate does not belong to the specified hiring drive");
        }

        return candidate;
    }

    private void validateInterviewerRole(User user, UUID interviewerId) {
        if (user.getRole() != Role.INTERVIEWER) {
            throw new IllegalArgumentException(
                    "User " + interviewerId + " does not have the INTERVIEWER role");
        }
    }

    private void validateInterviewerBelongsToCompany(User interviewer, UUID companyId, UUID interviewerId) {
        if (interviewer.getCompany() == null ||
                !interviewer.getCompany().getId().equals(companyId)) {
            throw new IllegalArgumentException(
                    "Interviewer " + interviewerId + " does not belong to the same company as the hiring drive");
        }
    }
}
