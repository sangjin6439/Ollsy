package kr.ollsy.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ExceptionResponse {

    private final String errorCode;
    private final String errorMessage;
}
