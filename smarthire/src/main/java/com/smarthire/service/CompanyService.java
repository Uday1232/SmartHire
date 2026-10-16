package com.smarthire.service;

import com.smarthire.dto.request.CreateCompanyRequest;
import com.smarthire.dto.request.CreateUserRequest;
import com.smarthire.dto.response.CompanyResponse;
import com.smarthire.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface CompanyService {

    CompanyResponse createCompany(CreateCompanyRequest request);

    UserResponse createCompanyAdmin(UUID companyId, CreateUserRequest request);

    List<CompanyResponse> getAllCompanies();

    List<UserResponse> getAdminsForCompany(UUID companyId);
}
