package com.smarthire.parser;

import com.smarthire.dto.request.CreateCandidateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ParsedRow {

    private final int rowNumber;

    private final CreateCandidateRequest request;

    private final String errorMessage;
}
