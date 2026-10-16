package com.smarthire.service.impl;

import com.smarthire.entity.Company;
import com.smarthire.entity.User;
import com.smarthire.exception.ResourceNotFoundException;
import com.smarthire.repository.UserRepository;
import com.smarthire.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final UserRepository userRepository;

    @Override
    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
    }

    @Override
    public Company getCurrentUserCompany() {
        User user = getCurrentUser();

        if (user.getCompany() == null) {
            throw new IllegalArgumentException("Current user is not assigned to a company");
        }

        return user.getCompany();
    }
}
