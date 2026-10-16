package com.smarthire.dto.request;

import com.smarthire.enums.Verdict;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluateQuestionRequest {

    private String notes;

    @NotNull
    private Verdict verdict;
}
