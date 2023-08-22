package com.studyex.global.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    NOT_SAME_PASSWORD_PASSWORD_CONFIRM(HttpStatus.BAD_REQUEST, "비밀번호와 비밀번호 확인이 불일치 합니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

}
