package com.smarthire.dto.response;

import com.smarthire.enums.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UserResponse {

    private UUID id;

    private String fullName;

    private String email;

    private Role role;

    private UUID companyId;

    private String companyName;
}
