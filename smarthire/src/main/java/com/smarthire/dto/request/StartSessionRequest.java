package com.smarthire.dto.request;

import com.smarthire.enums.DifficultyLevel;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartSessionRequest {

    @NotNull
    private DifficultyLevel difficultyLevel;

    @Min(1)
    @Max(50)
    private int questionCount;
}
