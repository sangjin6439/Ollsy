package kr.ollsy.global.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GlobalExceptionCode {

    // JwtExceptions
    UNAUTHORIZED("4011", "로그인이 필요합니다.", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN( "4012", "유효하지 않은 토큰입니다.",HttpStatus.UNAUTHORIZED),
    INVALID_ACCESS_TOKEN( "4013", "유효하지 않은 액세스 토큰입니다.",HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN("4014", "유효하지 않은 리프레쉬 토큰입니다.",HttpStatus.UNAUTHORIZED),

    //UserExceptions
    USER_NOT_FOUND("4041","사용자 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_NICKNAME("4091","중복된 닉네임입니다.",HttpStatus.CONFLICT),
    DUPLICATE_EMAIL("4092","이미 가입된 이메일입니다", HttpStatus.CONFLICT),

    //ItemExceptions
    ITEM_NOT_FOUND("4041","상품을 찾을 수 없습니다.",HttpStatus.NOT_FOUND),
    ITEM_VALID_NOT_NULL("4001","제품 이름, 제품 설명, 가격을 입력해 주세요.",HttpStatus.BAD_REQUEST),
    ITEM_VALID_NOT_BLANK("4002","제품 이름, 제품 설명은 비어있을 수 없습니다.",HttpStatus.BAD_REQUEST),
    ITEM_NOT_ENOUGH_STOCK("4003","재고가 없습니다.",HttpStatus.BAD_REQUEST),
    ITEM_INVALID_QUANTITY("4004", "수량은 1개 이상이어야 합니다.", HttpStatus.BAD_REQUEST),

    //CategoryExceptions
    CATEGORY_NOT_FOUND("4041","카테고리를 찾을 수 없습니다.",HttpStatus.NOT_FOUND),
    PARENT_NOT_FOUND("4042","상위 카테고리를 찾을 수 없습니다", HttpStatus.NOT_FOUND),

    //OrderExceptions
    ORDER_NOT_FOUND("4041","주문 정보를 찾을 수 없습니다.",HttpStatus.NOT_FOUND),

    //ItemImageExceptions
    ITEM_IMAGE_BAD_REQUEST("4001","파일을 업로드할 수 없습니다.", HttpStatus.BAD_REQUEST),
    ITEM_IMAGE_NOT_FOUND("4041","이미지 정보를 찾을 수 없습니다.",HttpStatus.NOT_FOUND);

    private final String errorCode;         // 에러 코드
    private final String errorMessage;      // 에러 메시지
    private final HttpStatus httpStatus;    // HTTP 상태 코드
}
