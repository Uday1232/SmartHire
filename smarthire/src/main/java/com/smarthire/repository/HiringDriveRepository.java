package com.smarthire.repository;

import com.smarthire.entity.HiringDrive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HiringDriveRepository extends JpaRepository<HiringDrive, UUID> {

    List<HiringDrive> findByCompanyIdOrderByCreatedAtDesc(UUID companyId);
}
