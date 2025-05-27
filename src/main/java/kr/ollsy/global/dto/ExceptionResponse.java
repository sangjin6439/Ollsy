package kr.ollsy.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public record ExceptionResponse(
        String errorCode,
        String errorMessage
) {
    public static ExceptionResponse of(String errorCode, String errorMessage) {
        return new ExceptionResponse(errorCode, errorMessage);
    }
}