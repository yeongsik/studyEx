package com.studyex.member.dto;

import com.studyex.member.entity.Member;
import com.studyex.member.entity.MemberType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponse {

    private Long id;

    private String email;

    private String name;

    private String phoneNumber;

    private MemberType memberType;

    @Builder
    public LoginResponse(Long id, String email, String name, String phoneNumber, MemberType memberType) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.memberType = memberType;
    }

    public static LoginResponse of(Member member) {
        return LoginResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .memberType(member.getMemberType())
                .build();
    }
}
