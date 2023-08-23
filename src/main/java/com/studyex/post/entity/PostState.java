package com.studyex.post.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostState {

    BEFORE_PROCESS("B","처리 대기"),
    ING_PROCESS("I", "처리 중"),
    REQUEST_FINISH("R", "완료 요청"),
    AGAIN_REQUEST("A", "재요청"),
    PROCESS_FINISH("F", "완료"),

    ;

    private final String code;
    private final String message;

}
