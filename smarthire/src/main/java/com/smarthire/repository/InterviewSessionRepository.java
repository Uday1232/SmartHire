package com.smarthire.repository;

import com.smarthire.entity.InterviewSession;
import com.smarthire.enums.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InterviewSessionRepository extends JpaRepository<InterviewSession, UUID> {

    Optional<InterviewSession> findByInterviewerIdAndCandidateIdAndInterviewRoundId(
            UUID interviewerId, UUID candidateId, UUID interviewRoundId);

    List<InterviewSession> findByCandidateIdAndStatus(UUID candidateId, SessionStatus status);

    List<InterviewSession> findByCandidateIdAndInterviewRoundIdAndStatus(
            UUID candidateId, UUID interviewRoundId, SessionStatus status);

    Optional<InterviewSession> findByCandidateIdAndInterviewerIdAndInterviewRoundId(
            UUID candidateId, UUID interviewerId, UUID interviewRoundId);

    Optional<InterviewSession> findByCandidateIdAndInterviewerIdAndInterviewRound_HiringDriveId(
            UUID candidateId, UUID interviewerId, UUID hiringDriveId);
}
