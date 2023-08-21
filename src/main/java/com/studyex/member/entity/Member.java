package com.studyex.member.entity;

import com.studyex.member.dto.SignUpRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String nickName;

    private String password;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @Builder
    public Member(Long id, String email, String nickName, String password, String phoneNumber, MemberType memberType) {
        this.id = id;
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.memberType = memberType;
    }

    public static Member of(SignUpRequest memberReq) {
        return Member.builder()
                .email(memberReq.getEmail())
                .nickName(memberReq.getNickName())
                .password(memberReq.getPassword())
                .phoneNumber(memberReq.getPhoneNumber())
                .memberType(MemberType.CUSTOMER)
                .build();
    }
}
