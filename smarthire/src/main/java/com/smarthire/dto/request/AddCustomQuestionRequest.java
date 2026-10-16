package com.smarthire.dto.request;

import com.smarthire.enums.DifficultyLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCustomQuestionRequest {

    @NotBlank
    private String questionText;

    @NotNull
    private DifficultyLevel difficultyLevel;
}
