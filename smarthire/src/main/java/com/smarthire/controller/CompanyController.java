package com.smarthire.controller;

import com.smarthire.dto.request.CreateCompanyRequest;
import com.smarthire.dto.request.CreateUserRequest;
import com.smarthire.dto.response.CompanyResponse;
import com.smarthire.dto.response.UserResponse;
import com.smarthire.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<CompanyResponse> createCompany(
            @Valid @RequestBody CreateCompanyRequest request
    ) {
        return new ResponseEntity<>(
                companyService.createCompany(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @PostMapping("/{companyId}/admins")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<UserResponse> createCompanyAdmin(
            @PathVariable UUID companyId,
            @Valid @RequestBody CreateUserRequest request
    ) {
        return new ResponseEntity<>(
                companyService.createCompanyAdmin(companyId, request),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{companyId}/admins")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<UserResponse>> getAdminsForCompany(
            @PathVariable UUID companyId
    ) {
        return ResponseEntity.ok(companyService.getAdminsForCompany(companyId));
    }
}
