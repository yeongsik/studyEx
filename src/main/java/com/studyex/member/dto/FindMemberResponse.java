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

    private String nickName;

    private String password;

    private String phoneNumber;

    private MemberType memberType;

    @Builder
    public FindMemberResponse(Long id, String email, String nickName, String password, String phoneNumber, MemberType memberType) {
        this.id = id;
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.memberType = memberType;
    }

    public static FindMemberResponse of(Member member) {
        return FindMemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickName(member.getNickName())
                .password(member.getPassword())
                .phoneNumber(member.getPhoneNumber())
                .memberType(member.getMemberType())
                .build();
    }

}
