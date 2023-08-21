package com.studyex.member.dto;

import com.studyex.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FindMemberResponse {

    private Long id;

    private String email;

    private String nickName;

    private String password;

    private String phoneNumber;

    @Builder
    public FindMemberResponse(Long id, String email, String nickName, String password, String phoneNumber) {
        this.id = id;
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public static FindMemberResponse of(Member member) {

        return FindMemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickName(member.getNickName())
                .password(member.getPassword())
                .phoneNumber(member.getPhoneNumber())
                .build();
    }

}
