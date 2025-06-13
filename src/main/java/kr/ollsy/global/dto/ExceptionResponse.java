package kr.ollsy.global.dto;

public record ExceptionResponse(
        String errorCode,
        String errorMessage
) {
    public static ExceptionResponse of(String errorCode, String errorMessage) {
        return new ExceptionResponse(errorCode, errorMessage);
    }
}