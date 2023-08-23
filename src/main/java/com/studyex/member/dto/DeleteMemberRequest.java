package com.studyex.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class DeleteMemberRequest {

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @Builder
    public DeleteMemberRequest(String password) {
        this.password = password;
    }
}
