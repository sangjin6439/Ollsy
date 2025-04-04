package kr.ollsy.global.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GlobalExceptionCode {

    // 400 서버 오류
    BAD_REQUEST("4001", "잘못된 요청입니다", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("4011", "로그인이 필요합니다.", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN( "4012", "유효하지 않은 토큰입니다.",HttpStatus.UNAUTHORIZED),
    INVALID_ACCESS_TOKEN( "4013", "유효하지 않은 액세스 토큰입니다.",HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN("4014", "유효하지 않은 리프레쉬 토큰입니다.",HttpStatus.UNAUTHORIZED),
    NOT_FOUND("4041", "찾을 수 없습니다", HttpStatus.NOT_FOUND),
    DUPLICATE_NICKNAME("4091","중복된 닉네임입니다.",HttpStatus.CONFLICT),

    //500 클라이언트 오류
    CLIENT_ERROR("5001", "클라이언트 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String errorCode;         // 에러 코드
    private final String errorMessage;      // 에러 메시지
    private final HttpStatus httpStatus;    // HTTP 상태 코드
}
