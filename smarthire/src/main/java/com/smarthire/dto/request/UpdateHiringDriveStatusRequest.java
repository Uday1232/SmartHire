package com.smarthire.dto.request;

import com.smarthire.enums.HiringDriveStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateHiringDriveStatusRequest {

    @NotNull(message = "Status must not be null")
    private HiringDriveStatus status;
}
