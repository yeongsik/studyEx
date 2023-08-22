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

    private String name;

    private String password;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @Builder
    public Member(Long id, String email, String name, String password, String phoneNumber, MemberType memberType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.memberType = memberType;
    }

    public static Member of(SignUpRequest memberReq) {
        return Member.builder()
                .email(memberReq.getEmail())
                .name(memberReq.getName())
                .password(memberReq.getPassword())
                .phoneNumber(memberReq.getPhoneNumber())
                .memberType(MemberType.CUSTOMER)
                .build();
    }
}
