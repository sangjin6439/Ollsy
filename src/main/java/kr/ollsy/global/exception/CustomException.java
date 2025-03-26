package kr.ollsy.global.exception;


import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {

    private final HttpStatus httpStatus; //Http 상태값
    private final String errorCode; //에러 코드
    private final String errorMessage; //에러 메시지

    public CustomException(GlobalExceptionCode globalExceptionCode) {
        this.httpStatus = globalExceptionCode.getHttpStatus();
        this.errorCode = globalExceptionCode.getErrorCode();
        this.errorMessage = globalExceptionCode.getErrorMessage();
    }

}
