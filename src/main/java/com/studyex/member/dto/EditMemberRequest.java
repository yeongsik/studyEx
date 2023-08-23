package com.studyex.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class EditMemberRequest {

    @NotBlank(message = "핸드폰 번호를 입력해주세요.")
    @Pattern(regexp = "^01\\d{8,9}$", message = "잘못된 형식 입니다.")
    private String phoneNumber;

    @Builder
    public EditMemberRequest(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
