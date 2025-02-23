package shop.shopBE.domain.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import shop.shopBE.global.exception.code.ExceptionCode;

@AllArgsConstructor
@Getter
public enum MemberExceptionCode implements ExceptionCode {
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),;


    private final HttpStatus httpStatus;
    private final String message;



}
