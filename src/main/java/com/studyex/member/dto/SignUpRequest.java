package com.studyex.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class SignUpRequest {

    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private String passwordConfirm;

    @NotBlank(message = "핸드폰 번호를 입력해주세요.")
    private String phoneNumber;

    @Builder
    public SignUpRequest(String email, String name, String password, String passwordConfirm, String phoneNumber) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.phoneNumber = phoneNumber;
    }
}
