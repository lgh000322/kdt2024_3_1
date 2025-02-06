package shop.shopBE.domain.destination.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import shop.shopBE.global.exception.code.ExceptionCode;


@AllArgsConstructor
@Getter
public enum DestinationException implements ExceptionCode {

    DESTINATION_NOT_FOUND(HttpStatus.NOT_FOUND, "회원의 배송지를 찾을 수 없습니다."),;

    private final HttpStatus httpStatus;
    private final String message;

}
