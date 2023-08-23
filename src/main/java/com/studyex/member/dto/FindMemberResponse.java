package com.studyex.member.dto;

import com.studyex.member.entity.Member;
import com.studyex.member.entity.MemberType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindMemberResponse {

    private Long id;

    private String email;

    private String name;

    private String password;

    private String phoneNumber;

    private MemberType memberType;

    @Builder
    public FindMemberResponse(Long id, String email, String name, String password, String phoneNumber, MemberType memberType) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.memberType = memberType;
    }

    public static FindMemberResponse of(Member member) {
        return FindMemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .password(member.getPassword())
                .phoneNumber(member.getPhoneNumber())
                .memberType(member.getMemberType())
                .build();
    }

}
