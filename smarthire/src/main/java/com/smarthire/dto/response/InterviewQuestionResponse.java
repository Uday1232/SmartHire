package com.smarthire.dto.response;

import com.smarthire.enums.DifficultyLevel;
import com.smarthire.enums.QuestionSource;
import com.smarthire.enums.Verdict;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class InterviewQuestionResponse {

    private UUID id;
    private String questionText;
    private DifficultyLevel difficultyLevel;
    private QuestionSource source;
    private String skill;
    private String notes;
    private Verdict verdict;
}
