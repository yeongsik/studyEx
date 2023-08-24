package com.studyex.post.entity;

import lombok.Getter;

@Getter
public enum PostType {
    ERROR("E" , "에러 처리"),
    DEVELOPMENT("D", "추가 개발"),

    ;

    PostType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private final String code;
    private final String message;



}
