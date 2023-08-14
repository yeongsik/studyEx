package com.studyex.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequest {

    private String email;

    private String nickName;

    private String password;

    private String passwordConfirm;

    private String phoneNumber;

    @Builder
    public SignUpRequest(String email, String nickName, String password, String passwordConfirm, String phoneNumber) {
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.phoneNumber = phoneNumber;
    }
}
