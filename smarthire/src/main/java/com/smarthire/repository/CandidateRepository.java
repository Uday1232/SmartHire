package com.smarthire.repository;

import com.smarthire.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, UUID> {

    boolean existsByHiringDriveIdAndEmail(UUID hiringDriveId, String email);

    List<Candidate> findByHiringDriveIdOrderByCreatedAtDesc(UUID hiringDriveId);
}
