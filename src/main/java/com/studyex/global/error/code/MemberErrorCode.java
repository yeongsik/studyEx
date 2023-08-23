package com.studyex.global.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    NOT_SAME_PASSWORD_PASSWORD_CONFIRM(HttpStatus.BAD_REQUEST, "비밀번호와 비밀번호 확인이 불일치 합니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일입니다."),
    EMPTY_MEMBER(HttpStatus.BAD_REQUEST, "조회되지 않은 회원입니다."),
    NOT_VALID_PWD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    NOT_LOGIN_MEMBER(HttpStatus.BAD_REQUEST, "로그인 하지 않았습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String message;

}
