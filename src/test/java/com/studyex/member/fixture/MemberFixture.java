package com.studyex.member.fixture;

import com.studyex.member.dto.SignUpRequest;

public class MemberFixture {
    public final static SignUpRequest NOT_SAME_PWD_PWD_CONFIRM_SIGN_UP_REQ = SignUpRequest.builder()
            .email("test@gmail.com")
            .name("테스트")
            .password("test@1234")
            .passwordConfirm("test@12345")
            .phoneNumber("01012345678")
            .build();

    public final static SignUpRequest SUCCESS_SIGN_UP_REQ = SignUpRequest.builder()
            .email("test@gmail.com")
            .name("테스트")
            .password("test@12345")
            .passwordConfirm("test@12345")
            .phoneNumber("01012345678")
            .build();
}
