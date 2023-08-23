package com.studyex.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class EditPwdRequest {

    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    private String curPassword;

    @NotBlank(message = "새 비밀번호를 입력해주세요.")
    private String newPassword;

    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private String passwordConfirm;

    @Builder
    public EditPwdRequest(String curPassword, String newPassword, String passwordConfirm) {
        this.curPassword = curPassword;
        this.newPassword = newPassword;
        this.passwordConfirm = passwordConfirm;
    }
}
