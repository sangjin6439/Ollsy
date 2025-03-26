package kr.ollsy.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

import kr.ollsy.global.dto.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponse> handleCustomExceptionHandler(CustomException ex) {
        log.warn("CustomException occurred!: ErrorCode = {}, Message = {}", ex.getErrorCode(), ex.getErrorMessage());

        ExceptionResponse exceptionResponse = ExceptionResponse.of(ex.getErrorCode(), ex.getErrorMessage());

        return ResponseEntity.status(ex.getHttpStatus()).body(exceptionResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGeneralException(Exception ex) {
        log.error("UnDefined exception occurred!", ex.getMessage(), ex);

        ExceptionResponse exceptionResponse = ExceptionResponse.of("UnKnown Error", "UnDefined exception occurred");

        return ResponseEntity.internalServerError().body(exceptionResponse);
    }
}