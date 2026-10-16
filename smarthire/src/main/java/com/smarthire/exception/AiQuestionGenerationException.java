package com.smarthire.exception;

public class AiQuestionGenerationException extends RuntimeException {

    public AiQuestionGenerationException(String message) {
        super(message);
    }

    public AiQuestionGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
