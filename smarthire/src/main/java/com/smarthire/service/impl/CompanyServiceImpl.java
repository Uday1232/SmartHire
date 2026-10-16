package com.smarthire.service.impl;

import com.smarthire.config.CacheConfig;
import com.smarthire.dto.request.CreateCompanyRequest;
import com.smarthire.dto.request.CreateUserRequest;
import com.smarthire.dto.response.CompanyResponse;
import com.smarthire.dto.response.UserResponse;
import com.smarthire.entity.Company;
import com.smarthire.entity.User;
import com.smarthire.enums.Role;
import com.smarthire.exception.ResourceAlreadyExistsException;
import com.smarthire.exception.ResourceNotFoundException;
import com.smarthire.repository.CompanyRepository;
import com.smarthire.repository.UserRepository;
import com.smarthire.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @CacheEvict(value = CacheConfig.COMPANIES, allEntries = true)
    public CompanyResponse createCompany(CreateCompanyRequest request) {
        if (companyRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Company email already exists");
        }

        Company company = new Company();
        company.setName(request.getName());
        company.setAddress(request.getAddress());
        company.setEmail(request.getEmail());

        return mapCompany(companyRepository.save(company));
    }

    @Override
    @CacheEvict(value = CacheConfig.INTERVIEWERS, allEntries = true)
    public UserResponse createCompanyAdmin(UUID companyId, CreateUserRequest request) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.COMPANY_ADMIN);
        user.setCompany(company);

        return mapUser(userRepository.save(user));
    }

    @Override
    @Cacheable(value = CacheConfig.COMPANIES)
    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapCompany)
                .toList();
    }

    @Override
    public List<UserResponse> getAdminsForCompany(UUID companyId) {
        companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        return userRepository.findByCompanyIdAndRole(companyId, Role.COMPANY_ADMIN)
                .stream()
                .map(this::mapUser)
                .toList();
    }

    private CompanyResponse mapCompany(Company company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .address(company.getAddress())
                .email(company.getEmail())
                .build();
    }

    private UserResponse mapUser(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .companyId(user.getCompany().getId())
                .companyName(user.getCompany().getName())
                .build();
    }
}
