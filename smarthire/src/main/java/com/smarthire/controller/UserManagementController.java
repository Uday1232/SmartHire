package com.smarthire.controller;

import com.smarthire.dto.request.CreateUserRequest;
import com.smarthire.dto.response.UserResponse;
import com.smarthire.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserManagementController {

    private final UserService userService;

    @PostMapping("/interviewers")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<UserResponse> createInterviewer(
            @Valid @RequestBody CreateUserRequest request
    ) {
        return new ResponseEntity<>(
                userService.createInterviewer(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/interviewers")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<List<UserResponse>> getInterviewers() {
        return ResponseEntity.ok(
                userService.getInterviewersForCurrentCompany()
        );
    }
}
