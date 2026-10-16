package com.smarthire.dto.response;

import com.smarthire.enums.RoundType;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class InterviewRoundResponse {

    private UUID id;
    private UUID hiringDriveId;
    private String name;
    private RoundType roundType;
}
