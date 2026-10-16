package com.smarthire.repository;

import com.smarthire.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

    boolean existsByEmail(String email);

    // Used by Super Admin to list all companies
    java.util.List<Company> findAllByOrderByCreatedAtDesc();
}
